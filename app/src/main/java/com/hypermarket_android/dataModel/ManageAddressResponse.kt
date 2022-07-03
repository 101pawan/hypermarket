package com.hypermarket_android.dataModel

data class ManageAddressResponse(
    val message: String,
    val status: Int,
    val user_address: List<UserAddres>
) {
    data class UserAddres(
        val alt_country_code: String,
        val alt_mobile_number: String,
        val building_tower: String,
        val city: Int,
        val created_at: String,
        val district: Int,
        val house_number: String,
        val id: Int,
        val name: String,
        val pincode: String,
        val save_as: String,
        val society_locality: String,
        val updated_at: String,
        val user_id: Int
    )
}