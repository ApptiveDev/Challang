package com.stellan.challang.data.api

import com.stellan.challang.data.model.auth.TokenStore
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val header = TokenStore.authHeaderOrNull()

        if (original.header("Authorization") != null) {
            return chain.proceed(original)
        }

        val req = if (header != null) {
            original.newBuilder().header("Authorization", header).build()
        } else original
        return chain.proceed(req)
    }
}