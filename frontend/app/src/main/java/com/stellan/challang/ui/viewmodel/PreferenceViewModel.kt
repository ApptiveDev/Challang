package com.stellan.challang.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stellan.challang.data.model.Preference.PreferenceRequest
import com.stellan.challang.data.repository.PreferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PreferenceViewModel(
    private val repository: PreferenceRepository
) : ViewModel() {

    private val _submitSuccess = MutableStateFlow<Boolean?>(null)
    val submitSuccess: StateFlow<Boolean?> = _submitSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun submitPreference(token: String, request: PreferenceRequest) {
        viewModelScope.launch {
            try {
                val response = repository.submitPreference(token, request)
                if (response.isSuccess) {
                    _submitSuccess.value = true
                } else {
                    _errorMessage.value = response.message
                    _submitSuccess.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = "서버 오류: ${e.message}"
                _submitSuccess.value = false
            }
        }
    }
}
