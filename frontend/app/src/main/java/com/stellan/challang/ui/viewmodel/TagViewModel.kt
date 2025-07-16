package com.stellan.challang.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stellan.challang.data.model.Tag.Tag
import com.stellan.challang.data.repository.TagRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TagViewModel(
    private val tagRepository: TagRepository
) : ViewModel() {

    private val _tagList = MutableStateFlow<List<Tag>>(emptyList())
    val tagList: StateFlow<List<Tag>> = _tagList.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchTags(token: String) {
        viewModelScope.launch {
            try {
                val response = tagRepository.fetchTags(token)
                if (response.isSuccess && response.result != null) {
                    _tagList.value = response.result
                } else {
                    _error.value = response.message
                }
            } catch (e: Exception) {
                _error.value = "태그 불러오기 실패: ${e.message}"
            }
        }
    }
}
