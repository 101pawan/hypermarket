package com.hypermarket_android.ui.login

data class ResponseLogin(
    var message: String,
    var status: Int,
    var user_data: UserData
)