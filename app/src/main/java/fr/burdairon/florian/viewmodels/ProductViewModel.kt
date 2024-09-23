package fr.burdairon.florian.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.burdairon.florian.model.Product
import fr.burdairon.florian.repositories.ProductRepository
import fr.burdairon.florian.utils.AppResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository): ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun getAll() {
        viewModelScope.launch {
            when (val result = repository.getAll()) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(productList = result.successData)
                    }
                }
                is AppResult.Error -> {} // handle error
            }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            when (repository.addProduct(product)) {
                is AppResult.Success -> {
                    val products = _uiState.value.productList.toMutableList()
                    products.add(product)
                    _uiState.update {
                        it.copy(productList = products)
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

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            when (repository.updateProduct(product)) {
                is AppResult.Success -> {
                    val products = _uiState.value.productList.toMutableList()
                    val index = products.indexOfFirst { it.id == product.id }
                    if (index != -1) {
                        products[index] = product
                    }
                    _uiState.update {
                        it.copy(productList = products)
                    }
                }
                is AppResult.Error -> {} // handle error
            }
        }
    }
}

data class MainUiState(
    val productList: List<Product> = emptyList()
)