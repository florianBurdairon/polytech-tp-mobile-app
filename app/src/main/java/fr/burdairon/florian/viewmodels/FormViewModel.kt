package fr.burdairon.florian.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.burdairon.florian.model.Product
import fr.burdairon.florian.repositories.ProductRepository
import fr.burdairon.florian.utils.AppResult
import kotlinx.coroutines.launch

class FormViewModel(private val repository: ProductRepository): ViewModel() {

    fun addProduct(product: Product) {
        viewModelScope.launch {
            when (val result = repository.addProduct(product)) {
                is AppResult.Success -> {
                    Log.d("db", "insert success")
                }
                is AppResult.Error -> {
                    // handle error
                    Log.e("ProductRepository", result.message)
                }
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            when (val result = repository.updateProduct(product)) {
                is AppResult.Success -> {
                    Log.d("db", "update success")
                }
                is AppResult.Error -> {
                    // handle error
                    Log.e("ProductRepository", result.message)
                }
            }
        }
    }
}