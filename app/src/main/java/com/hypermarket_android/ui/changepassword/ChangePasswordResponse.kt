package com.hypermarket_android.ui.changepassword

data class ChangePasswordResponse(
    val message: String,
    val status: Int,
    val user_data: UserData
)

data class UserData(
    val access_token: String,
    val country_code: String,
    val country_name: String,
    val created_at: String,
    val device_token: String,
    val device_type: Int,
    val email: String,
    val id: Int,
    val image: String,
    val is_block: Int,
    val is_register: Int,
    val mobile_number: String,
    val name: String,
    val otp: Any,
    val otp_verified: Int,
    val otp_verify_time: Any,
    val referCode: String,
    val shipping_address: Any,
    val social_id: Any,
    val social_type: Any,
    val updated_at: String
)