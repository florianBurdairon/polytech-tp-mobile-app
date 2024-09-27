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

    fun loadMore() {
        viewModelScope.launch {
            val currentList = _uiState.value.restaurantList
            val offset = currentList.size
            when (val result = repository.getRestaurants(offset)) {
                is AppResult.Success -> {
                    Log.d("api", "loadMore success")
                    _uiState.update {
                        it.copy(restaurantList = currentList + result.successData)
                    }
                }
                is AppResult.Error -> {} // handle error
            }
        }
    }
}