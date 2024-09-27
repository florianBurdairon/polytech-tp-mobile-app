package fr.burdairon.florian.api

import fr.burdairon.florian.model.RestaurantApiResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/explore/v2.1/catalog/datasets/osm-france-food-service/records")
    suspend fun getRestaurants(
        @Query("where") whereClause: String = "meta_code_com = '69266'",
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0
    ): RestaurantApiResult

}