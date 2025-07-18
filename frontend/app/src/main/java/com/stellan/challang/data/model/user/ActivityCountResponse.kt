package com.stellan.challang.data.model.user

data class ActivityCountResponse(
    val httpStatus: String,
    val isSuccess: Boolean,
    val message: String,
    val code: Int,
    val result: ActivityCount
)

data class ActivityCount(
    val likedCurationCount: Int,
    val writtenReviewCount: Int
)

