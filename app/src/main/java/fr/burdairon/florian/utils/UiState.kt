package fr.burdairon.florian.utils

import fr.burdairon.florian.model.Product
import fr.burdairon.florian.model.Restaurant

data class MainUiState(
    val productList: List<Product> = emptyList(),
    val favoriteList: List<Product> = emptyList()
)

data class RestaurantUiState(
    val restaurantList: List<Restaurant> = emptyList()
)