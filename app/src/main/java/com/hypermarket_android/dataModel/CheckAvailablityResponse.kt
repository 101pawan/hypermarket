package com.hypermarket_android.dataModel

data class CheckAvailablityResponse(
    val `data`: Data?,
    val is_available: String?, // 1
    val message: String? // success
)

data class Data(
    val created_at: String?,
    var deliveryTime: String?,
    val id: Int?,
    val price: String?,
    val updated_at: String?,
    val name: String?,
    val state_id: String?,
    val country_id: String?
)