package com.stellan.challang.data.model.auth

// 로그인 응답: httpStatus가 String 타입
data class ApiResponseStringStatus<T>(
    val httpStatus: String,
    val isSuccess: Boolean,
    val message: String?,
    val code: Int,
    val result: T?
)

// 회원가입 응답: httpStatus가 객체 타입인줄 알았는데 string
data class ApiResponseBooleanResult(
    val httpStatus: String,
    val isSuccess: Boolean,
    val message: String?,
    val code: Int,
    val result: Boolean?)


