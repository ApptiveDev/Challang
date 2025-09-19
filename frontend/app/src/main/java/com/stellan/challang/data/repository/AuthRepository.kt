package com.stellan.challang.data.repository

import android.content.Context
import com.stellan.challang.data.api.ApiClient
import com.stellan.challang.data.model.auth.TokenStore

class AuthRepository(private val context: Context) {
    private val apiService = ApiClient.api

    suspend fun clearTokens() {
        TokenStore.clearTokens()
    }


    sealed class KakaoLoginResult {
        data class Success(val accessToken: String, val refreshToken: String) : KakaoLoginResult()
        object NeedsSignup : KakaoLoginResult()
        object Failed : KakaoLoginResult()
    }
}