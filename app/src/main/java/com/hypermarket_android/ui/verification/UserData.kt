package com.hypermarket_android.ui.verification

data class UserData(
    var access_token: Any,
    var country_code: Any,
    var country_name: Any,
    var created_at: String,
    var device_token: String,
    var device_type: Int,
    var email: String,
    var id: Int,
    var image: Any,
    var is_block: Int,
    var is_register: Int,
    var mobile_number: String,
    var name: String,
    var otp: String,
    var otp_verified: Int,
    var shipping_address: Any,
    var social_id: Any,
    var social_type: Any,
    var updated_at: String
)