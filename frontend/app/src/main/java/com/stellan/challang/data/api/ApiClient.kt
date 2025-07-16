package com.stellan.challang.data.api

import com.stellan.challang.data.model.auth.TokenAuthenticator
import com.stellan.challang.data.model.auth.TokenProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient by lazy {
        val tokenManager = TokenProvider.get()

        OkHttpClient.Builder()
            .authenticator(TokenAuthenticator(tokenManager)) // 🔁 자동 토큰 갱신
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${tokenManager.getAccessToken() ?: ""}")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://54.161.220.185:8080/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val tagApi: TagApiService by lazy {
        retrofit.create(TagApiService::class.java)
    }

    val preferenceApi: PreferenceApiService by lazy {
        retrofit.create(PreferenceApiService::class.java)
    }

    val userApi: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }
}
