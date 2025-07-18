package com.stellan.challang.data.model.auth

import com.stellan.challang.util.TokenManager
import android.content.Context

object TokenProvider {
    private var tokenManager: TokenManager? = null

    // ✅ 초기화 함수 (Application 등에서 1회만 호출)
    fun init(context: Context) {
        if (tokenManager == null) {
            tokenManager = TokenManager(context)
        }
    }

    // ✅ tokenManager 접근
    fun get(): TokenManager {
        return tokenManager
            ?: throw IllegalStateException("TokenProvider.init(context)를 먼저 호출해야 해요!")
    }

    fun setAccessToken(token: String) {
        tokenManager?.saveAccessToken(token)
    }

    fun setRefreshToken(token: String) {
        tokenManager?.saveRefreshToken(token)
    }

    fun getAccessToken(): String? {
        return tokenManager?.getAccessToken()
    }

    fun getRefreshToken(): String? {
        return tokenManager?.getRefreshToken()
    }


    fun clearTokens() {
        tokenManager?.clearTokens()
    }
}
