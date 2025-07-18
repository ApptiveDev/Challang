package com.stellan.challang.data.model.drink

data class DrinkResponse(
    val httpStatus: String,
    val isSuccess: Boolean,
    val message: String,
    val code: Int,
    val result: List<Drink>?
)
