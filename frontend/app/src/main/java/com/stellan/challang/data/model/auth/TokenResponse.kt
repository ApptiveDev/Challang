package com.stellan.challang.data.model.auth

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)
