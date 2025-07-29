package com.stellan.challang.data.api

import com.stellan.challang.data.model.preference.PreferenceRequest
import com.stellan.challang.data.model.preference.PreferenceResponseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PreferenceApiService {
    @POST("api/user-preference")
    suspend fun setUserPreference(
        @Header("Authorization") token: String,
        @Body request: PreferenceRequest
    ): Response<PreferenceResponseResponse<String>>
}
