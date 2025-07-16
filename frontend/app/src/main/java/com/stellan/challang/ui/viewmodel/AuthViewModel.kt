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

    // safeApiCall 함수 추가 (private)
    private suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>,
        onUnauthorized: () -> Unit
    ): Response<T>? {
        return try {
            val response = apiCall()
            if (response.code() == 401) {
                TokenProvider.clearTokens()
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
        onSuccess: (Boolean) -> Unit,
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
                            TokenProvider.setAccessToken(tokens.accessToken)
                            TokenProvider.setRefreshToken(tokens.refreshToken)
                            _isLoggedIn.value = true
                            onSuccess(false)
                        } else {
                            Log.e("AuthViewModel", "토큰이 null이에요")
                            onError("응답이 비어 있어요")
                        }
                    }

                    404 -> {
                        Log.d("AuthViewModel", "404 - 회원가입이 필요해요")
                        onNeedSignup(kakaoAccessToken)}

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
        onSuccess: (Boolean) -> Unit, // 신규 유저인지
        onError: (String) -> Unit,
        onUnauthorized: (() -> Unit)? = null,
        onRetry: ((String) -> Unit)? = null // 닉네임 중복 시 재생성
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
                ) ?: return@launch  // 401 처리 후 null일 경우 중단

                if (signupResponse.isSuccessful && signupResponse.body()?.isSuccess == true) {
                    val success = signupResponse.body()?.result ?: false
                    if (success) {
                        // 회원가입 성공 후 로그인 호출
                        val signinResponse = safeApiCall(
                            apiCall = { apiService.kakaoSignin(KakaoSigninRequest(kakaoAccessToken)) },
                            onUnauthorized = { onUnauthorized?.invoke() }
                        ) ?: return@launch

                        if (signinResponse.isSuccessful && signinResponse.body()?.isSuccess == true) {
                            val tokens = signinResponse.body()?.result
                            if (tokens != null) {
                                TokenProvider.setAccessToken(tokens.accessToken)
                                TokenProvider.setRefreshToken(tokens.refreshToken)
                                onSuccess(true) // 신규 유저 & 로그인 성공
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
                    // 닉네임 중복 체크
                    val errorBody = signupResponse.errorBody()?.string()
                    if (errorBody?.contains("닉네임", ignoreCase = true) == true &&
                        errorBody.contains("중복", ignoreCase = true)) {
                        onRetry?.invoke("DUPLICATED") // 콜백으로 UI에서 닉네임 재생성
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
        val refreshToken = TokenProvider.get().getRefreshToken() ?: return false
        val accessToken = TokenProvider.get().getAccessToken() ?: return false
        return try {
            val response = safeApiCall(
                apiCall = { apiService.logout(refreshToken, "Bearer $refreshToken") },
                onUnauthorized = {
                    TokenProvider.clearTokens()
                    _isLoggedIn.value = false
                }
            ) ?: return false

            if (response.isSuccessful && response.body()?.isSuccess == true) {
                TokenProvider.clearTokens()
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
            val accessToken = TokenProvider.get().getAccessToken()
            val refreshToken = TokenProvider.get().getRefreshToken()

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
                        TokenProvider.clearTokens()
                        _isLoggedIn.value = false
                        onUnauthorized?.invoke()
                    }
                ) ?: return@launch

                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    TokenProvider.clearTokens()
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
