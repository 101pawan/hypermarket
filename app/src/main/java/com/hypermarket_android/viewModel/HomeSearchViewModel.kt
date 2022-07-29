package com.hypermarket_android.viewModel

import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.dataModel.HomeSearchResponse
import com.hypermarket_android.dataModel.TrendingDataModel
import retrofit2.Callback

class HomeSearchViewModel : BaseViewModel() {

    fun getsearchProduct(
        accessToken: String,
        storeId: String,
        page: String,
        product_name: String,
        callback: Callback<HomeSearchResponse>
    ) {
        apiInterface.getSearchProduct(accessToken, storeId, page,product_name).enqueue(callback)
    }
}