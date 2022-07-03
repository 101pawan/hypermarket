package com.hypermarket_android.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.dataModel.OrderListResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WalletViewModel: BaseViewModel() {

    var amount = MutableLiveData<String>()

    fun getWalletData(accessToken: String,user_id: String) {
        apiInterface.getWalletData(
            accessToken = accessToken,user_id = user_id
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                amount.value = it.wallet_amount
                       Log.e("testingdata",it.toString())
//                onSuccessUpdateOrderResponse(it)
            },
                {
//                    onErrorOrderList(it)
                })
    }
}