package com.stellan.challang.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stellan.challang.data.api.ApiService
import com.stellan.challang.data.model.auth.*
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow

class AuthViewModel(
    private val apiService: ApiService
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>,
        onUnauthorized: suspend () -> Unit
    ): Response<T>? {
        return try {
            val response = apiCall()
            if (response.code() == 401) {
                TokenStore.clearTokens()
                _isLoggedIn.value = false
                onUnauthorized()
                null
            } else {
                response
            }
        } catch (e: Exception) {
            null
        }
    }

    fun kakaoLogin(
        kakaoAccessToken: String,
        onSuccess: (Boolean, Boolean) -> Unit,
        onNeedSignup: (String) -> Unit,
        onError: (String) -> Unit,
        onUnauthorized: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {
                val response = safeApiCall(
                    apiCall = { apiService.kakaoSignin(KakaoSigninRequest(kakaoAccessToken)) },
                    onUnauthorized = {
                        onUnauthorized?.invoke()
                    }
                )

                if (response == null) {
                    Log.e("AuthViewModel", "response is null (probably 401 handled)")
                    return@launch
                }

                Log.d("AuthViewModel", "response.code() = ${response.code()}")
                Log.d("AuthViewModel", "isSuccessful = ${response.isSuccessful}")
                Log.d("AuthViewModel", "response.body() = ${response.body()}")
                Log.d("AuthViewModel", "errorBody = ${response.errorBody()?.string()}")

                when (response.code()) {
                    200 -> {
                        val tokens = response.body()?.result
                        if (tokens != null) {
                            TokenStore.saveAccessToken(tokens.accessToken)
                            TokenStore.saveRefreshToken(tokens.refreshToken)
                            _isLoggedIn.value = true
                            val result = response.body()?.result
                            if (result != null) {
                                onSuccess(false, result.isPreferenceSet)
                            } else {
                                onError("서버 응답이 비어 있습니다.")
                            }
                        } else {
                            Log.e("AuthViewModel", "토큰이 null이에요")
                            onError("응답이 비어 있어요")
                        }
                    }

                    404 -> {
                        Log.d("AuthViewModel", "404 - 회원가입이 필요해요")
                        onNeedSignup(kakaoAccessToken)
                    }

                    else -> onError("로그인 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "로그인 실패 예외", e)
                onError("네트워크 오류: ${e.message}")
            }
        }
    }


    fun kakaoSignup(
        kakaoAccessToken: String,
        nickname: String,
        gender: Int,
        birthDate: String,
        onSuccess: (Boolean) -> Unit,
        onError: (String) -> Unit,
        onUnauthorized: (() -> Unit)? = null,
        onRetry: ((String) -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {
                val request = KakaoSignupRequest(
                    accessToken = kakaoAccessToken,
                    nickname = nickname,
                    gender = gender,
                    birthDate = birthDate,
                    role = "ROLE_USER"
                )

                val signupResponse = safeApiCall(
                    apiCall = { apiService.kakaoSignup(request) },
                    onUnauthorized = { onUnauthorized?.invoke() }
                ) ?: return@launch

                if (signupResponse.isSuccessful && signupResponse.body()?.isSuccess == true) {
                    val success = signupResponse.body()?.result ?: false
                    if (success) {
                        val signinResponse = safeApiCall(
                            apiCall = { apiService.kakaoSignin(KakaoSigninRequest(kakaoAccessToken)) },
                            onUnauthorized = { onUnauthorized?.invoke() }
                        ) ?: return@launch

                        if (signinResponse.isSuccessful && signinResponse.body()?.isSuccess == true) {
                            val tokens = signinResponse.body()?.result
                            if (tokens != null) {
                                TokenStore.saveAccessToken(tokens.accessToken)
                                TokenStore.saveRefreshToken(tokens.refreshToken)
                                onSuccess(true)
                            } else {
                                onError("로그인 토큰 응답이 비어 있어요")
                            }
                        } else {
                            onError("로그인 실패: ${signinResponse.code()}")
                        }
                    } else {
                        onError("회원가입 실패: 서버에서 false 반환")
                    }
                } else {
                    val errorBody = signupResponse.errorBody()?.string()
                    if (errorBody?.contains("닉네임", ignoreCase = true) == true &&
                        errorBody.contains("중복", ignoreCase = true)
                    ) {
                        onRetry?.invoke("DUPLICATED")
                    } else {
                        onError("회원가입 실패: ${signupResponse.code()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "회원가입 실패", e)
                onError("네트워크 오류: ${e.message}")
            }
        }
    }


    suspend fun logout(): Boolean {
        val refreshToken = TokenStore.getRefreshToken() ?: return false
        val accessToken = TokenStore.getAccessToken() ?: return false
        return try {
            val response = safeApiCall(
                apiCall = { apiService.logout(refreshToken, "Bearer $refreshToken") },
                onUnauthorized = {
                    TokenStore.clearTokens()
                    _isLoggedIn.value = false
                }
            ) ?: return false

            if (response.isSuccessful && response.body()?.isSuccess == true) {
                TokenStore.clearTokens()
                _isLoggedIn.value = false
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    fun withdraw(
        reason: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onUnauthorized: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            val accessToken = TokenStore.getAccessToken()
            val refreshToken = TokenStore.getRefreshToken()

            if (accessToken == null || refreshToken == null) {
                onError("토큰이 없습니다")
                _isLoggedIn.value = false
                return@launch
            }

            try {
                val response = safeApiCall(
                    apiCall = {
                        apiService.withdraw(
                            refreshToken = refreshToken,
                            authorization = "Bearer $refreshToken",
                            request = WithdrawReasonRequest(reason)
                        )
                    },
                    onUnauthorized = {
                        TokenStore.clearTokens()
                        _isLoggedIn.value = false
                        onUnauthorized?.invoke()
                    }
                ) ?: return@launch

                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    TokenStore.clearTokens()
                    _isLoggedIn.value = false
                    onSuccess()
                } else {
                    onError("회원탈퇴 실패: ${response.code()} ${response.message()}")
                }

            } catch (e: Exception) {
                Log.e("AuthViewModel", "회원탈퇴 예외 발생", e)
                onError("오류: ${e.message}")
            }
        }
    }
}
