package com.stellan.challang.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stellan.challang.data.model.auth.TokenProvider
import com.stellan.challang.data.model.drink.Drink
import com.stellan.challang.data.model.drink.DrinkResponse
import com.stellan.challang.data.repository.DrinkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DrinkViewModel(
    private val repository: DrinkRepository
) : ViewModel() {

    private val _drinks = MutableStateFlow<List<Drink>>(emptyList())
    val drinks: StateFlow<List<Drink>> = _drinks.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _selectedDrink = MutableStateFlow<Drink?>(null)
    val selectedDrink: StateFlow<Drink?> = _selectedDrink.asStateFlow()

    fun selectDrink(drink: Drink) {
        _selectedDrink.value = drink
    }


    fun fetchDrinks() {
        viewModelScope.launch {
            try {
                val refreshToken = TokenProvider.getRefreshToken() ?: return@launch
                val response = repository.getRecommendedDrinks("Bearer $refreshToken")
                _drinks.value = response;
            } catch (e: Exception) {
                println("술 실패 ${e}")
                _error.value = "주류 불러오기 실패: ${e.message}"
            }
        }
    }
}
