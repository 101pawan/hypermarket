package com.hypermarket_android.ui.personalDetailActivity

data class UserData(
    var access_token: String,
    var country_code: Any,
    var country_name: Any,
    var created_at: String,
    var device_token: String,
    var device_type: Int,
    var email: String,
    var id: Int,
    var image: String,
    var is_block: Int,
    var is_register: Int,
    var mobile_number: String,
    var name: String,
    var otp: Any,
    var otp_verified: Int,
    var shipping_address: Any,
    var social_id: Any,
    var social_type: Any,
    var updated_at: String
)