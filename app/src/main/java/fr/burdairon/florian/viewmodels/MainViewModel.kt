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

    // Get all products from the database. Can be filtered by name.
    fun getAll(nameFilter: String = "") {
        viewModelScope.launch {
            when (val result = repository.getAll()) {
                is AppResult.Success -> {
                    Log.d("db", "getAll success")
                    _uiState.update { state ->
                        state.copy(productList = result.successData.filter { it.name.contains(nameFilter, ignoreCase = true) })
                    }
                }
                is AppResult.Error -> {
                    // handle error
                    Log.e("ProductRepository", result.message)
                }
            }
        }
    }

    // Get all favorite products from the database.
    fun getFavorite() {
        viewModelScope.launch {
            when (val result = repository.getFavorite()) {
                is AppResult.Success -> {
                    Log.d("db", "getFavorite success")
                    _uiState.update {
                        it.copy(favoriteList = result.successData)
                    }
                }
                is AppResult.Error -> {
                    // handle error
                    Log.e("ProductRepository", result.message)
                }
            }
        }
    }

    // Delete a product from the database.
    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            when (val result = repository.deleteProduct(product)) {
                is AppResult.Success -> {
                    Log.d("db", "delete success")
                    val products = _uiState.value.productList.toMutableList()
                    products.remove(product)
                    _uiState.update { mainUiState ->
                        mainUiState.copy(productList = products, favoriteList = products.filter { it.isFavorite })
                    }
                }
                is AppResult.Error -> {
                    // handle error
                    Log.e("ProductRepository", result.message)
                }
            }
        }
    }

    // Remove a product from the favorite list.
    fun removeFavorite(product: Product) {
        viewModelScope.launch {
            product.isFavorite = false
            when (val result = repository.updateProduct(product)) {
                is AppResult.Success -> {
                    Log.d("db", "update success")
                    val products = _uiState.value.productList.toMutableList()
                    val index = products.indexOfFirst { it.id == product.id }
                    products[index] = product
                    _uiState.update { mainUiState ->
                        mainUiState.copy(productList = products, favoriteList = products.filter { it.isFavorite })
                    }
                }
                is AppResult.Error -> {
                    // handle error
                    Log.e("ProductRepository", result.message)
                }
            }
        }
    }
}