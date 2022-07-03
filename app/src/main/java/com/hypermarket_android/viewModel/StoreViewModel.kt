package com.hypermarket_android.viewModel

import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.dataModel.StoreDataModel
import com.hypermarket_android.dataModel.StoreDetailDataModel
import retrofit2.Callback

class StoreViewModel : BaseViewModel() {

    fun getStoreList(country: String, callback: Callback<StoreDataModel>) {
        apiInterface.getStoreList(country).enqueue(callback)
    }

    fun getStoreDetail(storeId: Int, callback: Callback<StoreDetailDataModel>) {
        apiInterface.getStoreDetail(storeId).enqueue(callback)
    }
}