package com.hypermarket_android.ui.register

data class ResponseRegister(
    var message: String,
    var status: Int,
    var user_data: UserData
)