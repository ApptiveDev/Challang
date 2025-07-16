package com.stellan.challang.data.model.auth

data class TokenPairResponse(
    val accessToken: String,
    val refreshToken: String
)