package com.stellan.challang.data.model.auth

data class KakaoSignupRequest(
    val accessToken: String,
    val nickname: String,
    val gender: Int,
    val birthDate: String,
    val role: String = "ROLE_USER"
)