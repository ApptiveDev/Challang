package com.stellan.challang.data.repository

import android.content.Context
import android.util.Log
import com.kakao.sdk.user.UserApiClient
import com.stellan.challang.data.api.ApiClient
import com.stellan.challang.data.model.auth.KakaoSigninRequest
import com.stellan.challang.data.model.auth.KakaoSignupRequest
import com.stellan.challang.data.model.auth.TokenProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import com.stellan.challang.data.model.auth.WithdrawReasonRequest
import com.stellan.challang.data.model.auth.TokenPairResponse

class AuthRepository(private val context: Context) {
    private val apiService = ApiClient.api

    fun clearTokens() {
        TokenProvider.clearTokens()
    }


    sealed class KakaoLoginResult {
        data class Success(val accessToken: String, val refreshToken: String) : KakaoLoginResult()
        object NeedsSignup : KakaoLoginResult()
        object Failed : KakaoLoginResult()
    }
}