package com.hypermarket_android.presenter


interface SignUpViewInterface {

    fun showToast(str: String?)
    fun displayError(s: String?)
    fun showProgressBar()
    fun hideProgressBar()

}