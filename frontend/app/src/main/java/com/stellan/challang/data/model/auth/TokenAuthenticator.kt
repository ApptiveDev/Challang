package com.stellan.challang.data.model.auth

import com.stellan.challang.data.api.ApiService
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.stellan.challang.util.TokenManager

class TokenAuthenticator(
    private val tokenManager: TokenManager
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = tokenManager.getRefreshToken() ?: return null

        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.161.220.185:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(ApiService::class.java)

        val refreshResponse = api.reissueAccessTokenSyncRaw(refreshToken).execute()


        val newTokenPair = refreshResponse.body()?.result ?: return null

        tokenManager.saveAccessToken(newTokenPair.accessToken)
        tokenManager.saveRefreshToken(newTokenPair.refreshToken)

        return response.request.newBuilder()
            .header("Authorization", "Bearer ${newTokenPair.accessToken}")
            .build()
    }
}