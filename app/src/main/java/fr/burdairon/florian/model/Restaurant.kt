package fr.burdairon.florian.model

import java.util.Date


data class Restaurant (
    var name: String? = null,
    var type: String? = null,
    var operator: Any? = null,
    var brand: String? = null,
    var cuisine: List<String>? = null,
    var vegetarian: String? = null,
    var vegan: String? = null,
    var opening_hours: String? = null,
    var wheelchair: String? = null,
    var delivery: String? = null,
    var takeaway: String? = null,
    var drive_through: String? = null,
    var internet_access: String? = null,
    var capacity: Any? = null,
    var stars: Any? = null,
    var smoking: String? = null,
    var wikidata: Any? = null,
    var brand_wikidata: Any? = null,
    var siret: String? = null,
    var phone: String? = null,
    var website: String? = null,
    var facebook: String? = null,
    var meta_name_com: String? = null,
    var meta_code_com: String? = null,
    var meta_name_dep: String? = null,
    var meta_code_dep: String? = null,
    var meta_name_reg: String? = null,
    var meta_code_reg: String? = null,
    var meta_geo_point: MetaGeoPoint? = null,
    var meta_osm_id: String? = null,
    var meta_osm_url: String? = null,
    var meta_first_update: Date? = null,
    var meta_last_update: String? = null,
    var meta_versions_number: Int = 0,
    var meta_users_number: Int = 0
)

data class RestaurantApiResult (
    var total_count: Int = 0,
    var results: List<Restaurant>? = null
)

data class MetaGeoPoint (
    var lon: Double = 0.0,
    var lat: Double = 0.0
)