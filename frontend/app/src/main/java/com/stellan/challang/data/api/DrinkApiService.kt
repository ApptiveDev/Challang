package com.stellan.challang.data.api

import com.stellan.challang.data.model.drink.DrinkResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface DrinkApiService {
    @GET("api/recommend")
    suspend fun getRecommendedDrinks(
        @Header("Authorization") token: String
    ): Response<DrinkResponse>

}