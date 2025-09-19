package com.stellan.challang.data.model.auth

import android.content.Context
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.stellan.challang.data.crypto.Crypto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

val Context.secretsDataStore by preferencesDataStore(name = "secrets")

object TokenStore {
    private val ACCESS_CT = stringPreferencesKey("access_ct")
    private val ACCESS_IV = stringPreferencesKey("access_iv")
    private val REFRESH_CT = stringPreferencesKey("refresh_ct")
    private val REFRESH_IV = stringPreferencesKey("refresh_iv")

    @Volatile private var store: DataStore<Preferences>? = null
    @Volatile private var accessCache: String? = null
    @Volatile private var refreshCache: String? = null

    fun init(context: Context) {
        if (store == null) {
            synchronized(this) {
                if (store == null) {
                    store = context.applicationContext.secretsDataStore
                }
            }
        }
    }

    private fun ds(): DataStore<Preferences> =
        store ?: error("TokenStore.init(context)를 Application에서 먼저 호출하세요.")

    suspend fun saveAccessToken(token: String) {
        val (iv, ct) = Crypto.encrypt(token)
        ds().edit {
            it[ACCESS_CT] = Base64.encodeToString(ct, Base64.NO_WRAP)
            it[ACCESS_IV] = Base64.encodeToString(iv, Base64.NO_WRAP)
        }
        accessCache = token
    }
    suspend fun saveRefreshToken(token: String) {
        val (iv, ct) = Crypto.encrypt(token)
        ds().edit {
            it[REFRESH_CT] = Base64.encodeToString(ct, Base64.NO_WRAP)
            it[REFRESH_IV] = Base64.encodeToString(iv, Base64.NO_WRAP)
        }
        refreshCache = token
    }

    fun saveAccessTokenSync(token: String) =
        runBlocking(Dispatchers.IO) { saveAccessToken(token) }
    fun saveRefreshTokenSync(token: String) =
        runBlocking(Dispatchers.IO) { saveRefreshToken(token) }

    suspend fun getAccessToken(): String? {
        accessCache?.let { return it }
        val prefs = ds().data.first()
        val token = decodeToken(prefs, ACCESS_CT, ACCESS_IV)
        accessCache = token
        return token
    }
    suspend fun getRefreshToken(): String? {
        refreshCache?.let { return it }
        val prefs = ds().data.first()
        val token = decodeToken(prefs, REFRESH_CT, REFRESH_IV)
        refreshCache = token
        return token
    }

    fun getAccessTokenBlocking(): String? =
        runBlocking(Dispatchers.IO) { getAccessToken() }
    fun getRefreshTokenBlocking(): String? =
        runBlocking(Dispatchers.IO) { getRefreshToken() }

    suspend fun clearTokens() {
        ds().edit { it.clear() }
        accessCache = null
        refreshCache = null
    }

    fun authHeaderOrNull(): String? =
        getAccessTokenBlocking()?.let { "Bearer $it" }

    fun isLoggedIn(): Boolean = getAccessTokenBlocking().isNullOrEmpty().not()

    private fun decodeToken(
        prefs: Preferences,
        ctKey: Preferences.Key<String>,
        ivKey: Preferences.Key<String>
    ): String? {
        val ctB64 = prefs[ctKey] ?: return null
        val ivB64 = prefs[ivKey] ?: return null
        val ct = Base64.decode(ctB64, Base64.NO_WRAP)
        val iv = Base64.decode(ivB64, Base64.NO_WRAP)
        return Crypto.decrypt(iv, ct)
    }
}