package com.hypermarket_android.viewModel

import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.dataModel.RandomProductDataModel
import com.hypermarket_android.dataModel.TrendingDataModel
import retrofit2.Callback

class TrendingViewModel: BaseViewModel() {

    fun getTrendingProducts(accessToken:String,storeId:String,page:String,callback: Callback<TrendingDataModel>) {
        apiInterface.getTrendingProduct(accessToken,storeId,page).enqueue(callback)
    }

    fun getAllTrendingProducts(accessToken:String,callback: Callback<TrendingDataModel>) {
        apiInterface.getTrendingProduct(accessToken).enqueue(callback)
    }

    fun getAllRandomProducts(accessToken:String,category_id:String,callback: Callback<RandomProductDataModel>) {
        apiInterface.getRandomProduct(accessToken,category_id).enqueue(callback)
    }
}