package fr.burdairon.florian.repositories

import fr.burdairon.florian.api.RetrofitInstance
import fr.burdairon.florian.model.Restaurant
import fr.burdairon.florian.utils.AppResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface RestaurantRepository {
    suspend fun getRestaurants(offset: Int = 0): AppResult<List<Restaurant>>
}

class RestaurantRepositoryImpl : RestaurantRepository {
    private val apiService = RetrofitInstance.api

    override suspend fun getRestaurants(offset: Int): AppResult<List<Restaurant>> {
        val data = withContext(Dispatchers.IO) {
            apiService.getRestaurants(offset = offset)
        }
        return if (data.total_count > 0) {
            AppResult.Success(data.results ?: emptyList())
        } else {
            AppResult.Error(Exception("Get no data from API"))
        }
    }
}