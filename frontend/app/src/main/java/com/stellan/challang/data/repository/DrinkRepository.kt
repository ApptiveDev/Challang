package com.stellan.challang.data.repository

import com.stellan.challang.data.api.DrinkApiService
import com.stellan.challang.data.model.drink.Drink

class DrinkRepository(
    private val apiService: DrinkApiService
) {
    suspend fun getRecommendedDrinks(token: String): List<Drink> {
        val response = apiService.getRecommendedDrinks(token);

        return when {
            response.isSuccessful && response.body() != null -> {
                response.body()!!.result!!
            }

            response.code() == 401 -> {
                throw Exception("인증 실패: 토큰이 유효하지 않거나 만료됨 (401)")
            }

            else -> {
                throw Exception("주류 추천 실패: ${response.code()} - ${response.message()}")
            }
        }
    }
}
