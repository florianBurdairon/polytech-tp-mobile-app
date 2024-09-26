package fr.burdairon.florian.api

import fr.burdairon.florian.model.RestaurantApiResult
import retrofit2.http.GET

interface ApiService {
    @GET("api/explore/v2.1/catalog/datasets/osm-france-food-service/records?where=meta_code_com%20%3D%20%0969266&limit=100")
    suspend fun getRestaurants(): RestaurantApiResult

}