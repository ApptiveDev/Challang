package com.stellan.challang.data.model.preference

data class PreferenceResponseResponse<T>(
    val httpStatus: Any?,
    val isSuccess: Boolean,
    val message: String,
    val code: Int,
    val result: T?
)
