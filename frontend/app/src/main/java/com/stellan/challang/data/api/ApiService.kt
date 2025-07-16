package com.stellan.challang.data.api

import com.stellan.challang.data.model.auth.*
import retrofit2.Response
import retrofit2.http.*
import retrofit2.Call

interface ApiService {

    @POST("api/auth/kakao/signin") // 로그인
    suspend fun kakaoSignin(
        @Body body: KakaoSigninRequest
    ): Response<KakaoSignInResponse>

    @POST("api/auth/kakao/signup") // 회원가입
    suspend fun kakaoSignup(
        @Body request: KakaoSignupRequest
    ): Response<ApiResponseBooleanResult>

    @POST("api/auth/refresh-token") // access token 재발급
    suspend fun reissueAccessToken(
        @Header("Authorization") refreshToken: String
    ): Response<ApiResponseStringStatus<TokenPairResponse>>

    @POST("api/auth/refresh-token")
    fun reissueAccessTokenSync( // ✅ 이름 구분해주면 좋아요
        @Header("Authorization") refreshToken: String
    ): Call<ApiResponseStringStatus<TokenPairResponse>>

    @POST("api/auth/refresh-token")
    fun reissueAccessTokenSyncRaw(
        @Header("Refresh-Token") refreshToken: String
    ): Call<ApiResponseStringStatus<TokenPairResponse>>



    @DELETE("api/auth/logout") // 로그아웃
    suspend fun logout(
        @Header("Refresh-Token") refreshToken: String,
        @Header("Authorization") authorization: String
    ): Response<ApiResponseStringStatus<String>>

    @HTTP(method = "DELETE", path = "api/auth/user", hasBody = true) // 회원탈퇴
    suspend fun withdraw(
        @Header("Authorization") authorization: String,
        @Header("Refresh-Token") refreshToken: String,
        @Body request: WithdrawReasonRequest
    ): Response<ApiResponseStringStatus<String>>
}
