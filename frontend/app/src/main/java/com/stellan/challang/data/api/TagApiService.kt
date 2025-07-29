package com.stellan.challang.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import com. stellan. challang. data. model. tag. TagResponse

interface TagApiService {
    @GET("/api/tags")
    suspend fun getAllTags(
        @Header("Authorization") token: String
    ): Response<TagResponse>
}
