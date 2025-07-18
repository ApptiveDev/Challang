package com.stellan.challang.data.repository

import com.stellan.challang.data.api.TagApiService
import com.stellan.challang.data.model.Tag.TagResponse

class TagRepository(
    private val apiService: TagApiService
) {
    suspend fun fetchTags(token: String): TagResponse {
        val response = apiService.getAllTags(token)

        return when {
            response.isSuccessful && response.body() != null -> {
                response.body()!!
            }

            response.code() == 401 -> {
                throw Exception("인증 실패: 토큰이 유효하지 않거나 만료됨 (401)")
            }

            else -> {
                throw Exception("태그 불러오기 실패: ${response.code()} - ${response.message()}")
            }
        }
    }
}
