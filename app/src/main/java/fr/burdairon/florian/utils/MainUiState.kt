package fr.burdairon.florian.utils

import fr.burdairon.florian.model.Product

data class MainUiState(
    val productList: List<Product> = emptyList(),
    val favoriteList: List<Product> = emptyList()
)