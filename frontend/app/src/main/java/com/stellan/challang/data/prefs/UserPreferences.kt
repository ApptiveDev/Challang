package com.stellan.challang.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray

val Context.userPrefsDataStore by preferencesDataStore(name = "user_prefs")
private val RECENT_SEARCHES_KEY = stringPreferencesKey("recent_searches")
private val GUIDE_SHOWN_KEY = booleanPreferencesKey("guide_shown")


private fun encodeList(list: List<String>) = JSONArray(list).toString()
private fun decodeList(json: String?): List<String> {
    if (json.isNullOrBlank()) return emptyList()
    val arr = JSONArray(json)
    return buildList {
        for (i in 0 until arr.length()) add(arr.optString(i))
    }
}

suspend fun Context.addRecentSearch(query: String, limit: Int = 3) {
    userPrefsDataStore.edit { prefs ->
        val current = decodeList(prefs[RECENT_SEARCHES_KEY])

        fun norm(s: String) = s.trim().lowercase()

        val q = query.trim()

        val updated = (sequenceOf(q) + current.asSequence())
            .filterIndexed { index, s -> index == 0 || norm(s) != norm(q) }
            .distinctBy { norm(it) }
            .take(limit)
            .toList()

        prefs[RECENT_SEARCHES_KEY] = encodeList(updated)
    }
}

fun Context.recentSearchesFlow(): Flow<List<String>> =
    userPrefsDataStore.data.map { prefs -> decodeList(prefs[RECENT_SEARCHES_KEY]) }

fun Context.hasSeenGuideFlow(): Flow<Boolean> =
    userPrefsDataStore.data.map { prefs -> prefs[GUIDE_SHOWN_KEY] ?: false }

suspend fun Context.setGuideShown() {
    userPrefsDataStore.edit { prefs -> prefs[GUIDE_SHOWN_KEY] = true }
}