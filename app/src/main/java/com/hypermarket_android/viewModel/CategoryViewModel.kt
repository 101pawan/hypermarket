package com.hypermarket_android.viewModel

import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.dataModel.CategoriesDataModel
import retrofit2.Callback

class CategoryViewModel : BaseViewModel() {

    fun getCategory(accessToken: String,storeId:Int,callback: Callback<CategoriesDataModel>) {
        apiInterface.getCategory(accessToken,storeId).enqueue(callback)
    }

}