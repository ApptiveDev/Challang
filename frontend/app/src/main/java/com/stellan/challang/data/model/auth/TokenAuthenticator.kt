package com.stellan.challang.data.model.auth

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.stellan.challang.data.api.ApiService

class TokenAuthenticator : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null

        val refreshToken = TokenStore.getRefreshTokenBlocking() ?: return null

        val retrofit = Retrofit.Builder()
            .baseUrl("http://54.161.220.185:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(ApiService::class.java)

        val refreshResponse = api.reissueAccessTokenSyncRaw(refreshToken).execute()
        val newPair = refreshResponse.body()?.result ?: return null

        TokenStore.saveAccessTokenSync(newPair.accessToken)
        TokenStore.saveRefreshTokenSync(newPair.refreshToken)

        return response.request.newBuilder()
            .header("Authorization", "Bearer ${newPair.accessToken}")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var r: Response? = response
        var count = 1
        while (r?.priorResponse != null) { count++; r = r.priorResponse }
        return count
    }
}