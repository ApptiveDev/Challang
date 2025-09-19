package com.stellan.challang.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stellan.challang.data.model.user.UserInfo
import com.stellan.challang.data.model.user.ActivityCount
import com.stellan.challang.data.repository.UserRepository
import com.stellan.challang.data.model.auth.TokenStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> = _userInfo

    private val _activityCount = MutableStateFlow<ActivityCount?>(null)
    val activityCount: StateFlow<ActivityCount?> = _activityCount

    fun fetchUserInfo() {
        viewModelScope.launch {
            try {
                val refreshToken = TokenStore.getRefreshToken() ?: return@launch
                val response = repository.getMyInfo("Bearer $refreshToken")
                if (response.isSuccessful) {
                    _userInfo.value = response.body()?.result
                } else {
                    println("getMyInfo 실패: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

fun fetchActivityCounts() {
    viewModelScope.launch {
        try {
            val refreshToken = TokenStore.getRefreshToken() ?: return@launch
            val response = repository.getActivityCounts("Bearer $refreshToken")

            if (response.isSuccessful) {
                _activityCount.value = response.body()?.result
            } else {
                println("❌ 실패: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

}
