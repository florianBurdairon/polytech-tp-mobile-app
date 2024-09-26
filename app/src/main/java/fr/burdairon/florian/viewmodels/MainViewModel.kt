package fr.burdairon.florian.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.burdairon.florian.model.Product
import fr.burdairon.florian.repositories.ProductRepository
import fr.burdairon.florian.utils.AppResult
import fr.burdairon.florian.utils.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val repository: ProductRepository): ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun getAll() {
        viewModelScope.launch {
            when (val result = repository.getAll()) {
                is AppResult.Success -> {
                    Log.d("db", "getAll success")
                    _uiState.update {
                        it.copy(productList = result.successData)
                    }
                }
                is AppResult.Error -> {} // handle error
            }
        }
    }

    fun getFavorite() {
        viewModelScope.launch {
            when (val result = repository.getFavorite()) {
                is AppResult.Success -> {
                    Log.d("db", "getFavorite success")
                    _uiState.update {
                        it.copy(favoriteList = result.successData)
                    }
                }
                is AppResult.Error -> {} // handle error
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            when (repository.deleteProduct(product)) {
                is AppResult.Success -> {
                    Log.d("db", "delete success")
                    val products = _uiState.value.productList.toMutableList()
                    products.remove(product)
                    _uiState.update {
                        it.copy(productList = products)
                    }
                }
                is AppResult.Error -> {} // handle error
            }
        }
    }

}