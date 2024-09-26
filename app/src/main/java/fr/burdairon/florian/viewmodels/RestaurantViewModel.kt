package fr.burdairon.florian.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.burdairon.florian.repositories.RestaurantRepository
import fr.burdairon.florian.utils.AppResult
import fr.burdairon.florian.utils.RestaurantUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RestaurantViewModel(private val repository: RestaurantRepository): ViewModel() {

    private val _uiState = MutableStateFlow(RestaurantUiState())
    val uiState: StateFlow<RestaurantUiState> = _uiState.asStateFlow()

    fun getRestaurants() {
        viewModelScope.launch {
            when (val result = repository.getRestaurants()) {
                is AppResult.Success -> {
                    Log.d("api", "getRestaurants success")
                    _uiState.update {
                        it.copy(restaurantList = result.successData)
                    }
                }
                is AppResult.Error -> {} // handle error
            }
        }
    }
}