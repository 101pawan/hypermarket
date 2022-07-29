package com.hypermarket_android.dataModel


data class CityResponse(
    var data: List<City>,
    var message: String
) {
    data class City(var id: Int, var country_id: Int, var name: String, var state_id: Int)
}
