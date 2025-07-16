package com.stellan.challang.data.api

import com.stellan.challang.data.model.user.UserInfoResponse
import com.stellan.challang.data.model.user.ActivityCountResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface UserApiService {

    @GET("/api/users/me")
    suspend fun getMyInfo(
        @Header("Authorization") token: String
    ): Response<UserInfoResponse>

    @GET("/api/users/activity-counts")
    suspend fun getActivityCounts(
        @Header("Authorization") token: String
    ): Response<ActivityCountResponse>
}
