package com.stellan.challang.data.repository

import com.stellan.challang.data.api.UserApiService
import com.stellan.challang.data.model.user.UserInfoResponse
import com.stellan.challang.data.model.user.ActivityCountResponse
import retrofit2.Response

class UserRepository(
    private val userApi: UserApiService
) {
    suspend fun getMyInfo(token: String): Response<UserInfoResponse> {
        return userApi.getMyInfo(token)
    }

    suspend fun getActivityCounts(token: String): Response<ActivityCountResponse> {
        return userApi.getActivityCounts(token)
    }
}
