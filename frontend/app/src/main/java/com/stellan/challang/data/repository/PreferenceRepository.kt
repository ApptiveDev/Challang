package com.stellan.challang.data.repository

import com.stellan.challang.data.api.PreferenceApiService
import com.stellan.challang.data.model.Preference.PreferenceRequest
import com.stellan.challang.data.model.Preference.PreferenceResponseResponse

class PreferenceRepository(
    private val apiService: PreferenceApiService
) {
    suspend fun submitPreference(
        token: String,
        request: PreferenceRequest
    ): PreferenceResponseResponse<String> {
        val response = apiService.setUserPreference(token, request)

        return when {
            response.isSuccessful && response.body() != null -> {
                response.body()!!
            }

            response.code() == 401 -> {
                throw Exception("인증 실패: 토큰이 유효하지 않거나 만료됨 (401)")
            }

            else -> {
                throw Exception("선호도 설정 실패: ${response.code()} - ${response.message()}")
            }
        }
    }
}
