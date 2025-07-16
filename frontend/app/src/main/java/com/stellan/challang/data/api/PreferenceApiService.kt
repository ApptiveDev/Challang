package com.stellan.challang.data.api

import com.stellan.challang.data.model.Preference.PreferenceRequest
import com.stellan.challang.data.model.Preference.PreferenceResponseResponse
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
