package com.stellan.challang.data.model.user

data class UserInfoResponse(
    val httpStatus: String,
    val isSuccess: Boolean,
    val message: String,
    val code: Int,
    val result: UserInfo
)

data class UserInfo(
    val nickname: String,
    val gender: Int,
    val birthDate: String
)

