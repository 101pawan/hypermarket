package com.hypermarket_android.base

import androidx.lifecycle.ViewModel
import com.hypermarket_android.util.ApiInterface
import com.hypermarket_android.util.RetrofitUtil

open class BaseViewModel : ViewModel() {

    val apiInterface: ApiInterface by lazy {
        RetrofitUtil.apiService()
    }
}