package com.statusapp.socialLogin.facebook

interface OnFacebookLoginListener {
    fun onFacebookLogin(name: String, email: String, id: String, imageUrl: String, gender: String)
    fun onFacebookError(message: String)
}
