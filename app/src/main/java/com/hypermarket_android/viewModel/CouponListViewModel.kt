package com.hypermarket_android.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.dataModel.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
class CouponListViewModel : BaseViewModel() {


    var couponListResponse = MutableLiveData<CouponListResponse>()
    var errorOrderList = MutableLiveData<Throwable>()


    //    getCoupons
    @SuppressLint("CheckResult")
    fun getCoupons(accessToken: String, store_id: String, product_id: String) {
        Log.e("getCoupons","calling")
        apiInterface.get_coupon(
            accessToken = accessToken,
            storeId = store_id,
            ProductId = product_id

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.e("onSuccessCouponList",it.toString()+" == "+"test")
                onSuccessCouponList(it)
            },
                {
                    Log.e("onErrorOrderList",it.toString()+" == "+"test")
                    onErrorOrderList(it)
                })
    }


    private fun onSuccessCouponList(it: CouponListResponse) {
        Log.e("onSuccessCouponList",it.toString())
        couponListResponse.value = it
    }


    private fun onErrorOrderList(it: Throwable) {
        errorOrderList.value = it
    }






}