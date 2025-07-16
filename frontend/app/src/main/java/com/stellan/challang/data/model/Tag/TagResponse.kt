package com.stellan.challang.data.model.Tag

data class TagResponse(
    val httpStatus: String,
    val isSuccess: Boolean,
    val message: String,
    val code: Int,
    val result: List<Tag>?
)
