package com.hypermarket_android.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.dataModel.CheckAvailablityResponse
import com.hypermarket_android.dataModel.DeleteAddressResponse
import com.hypermarket_android.dataModel.ManageAddressResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SelectAddressViewModel : BaseViewModel() {
    private lateinit var mDisposable: Disposable
    var manageAddressResponse = MutableLiveData<ManageAddressResponse>()
    var errorManageAddress = MutableLiveData<Throwable>()

    var deleteAddressResponse = MutableLiveData<DeleteAddressResponse>()
    var errorDeleteAddress = MutableLiveData<Throwable>()

    var checkAvailablityResponse  = MutableLiveData<CheckAvailablityResponse>()

    @SuppressLint("CheckResult")
    fun productServiceCharge(
        accessToken:String,
        zipcode:String
    ){
        apiInterface.productServiceCharge(
            accessToken = accessToken,
            zipcode = zipcode
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccess(it)
            },
                {
                    onErrorDeleteAddress(it)
                })
    }

    private fun onSuccess(it: CheckAvailablityResponse?) {
        checkAvailablityResponse.value =it
    }


    //    hitAddNewAddress
    fun hitManageAddressApi(accessToken: String) {
        mDisposable = apiInterface.getMangeAddressList(
            accessToken = accessToken,

            ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessManageAddress(it)
            },
                {
                    onErrorAddNewAddress(it)
                })
    }

    private fun onSuccessManageAddress(it: ManageAddressResponse) {
        manageAddressResponse.value = it
    }

    private fun onErrorAddNewAddress(it: Throwable) {
        errorManageAddress.value = it
    }


    fun hitDeletAddressApi(accessToken: String ,addressId : String) {
        mDisposable = apiInterface.deleteAddress(
            accessToken = accessToken,
            addressId = addressId
            ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessDeleteAddress(it)
            },
                {
                    onErrorDeleteAddress(it)
                })
    }

    private fun onSuccessDeleteAddress(it: DeleteAddressResponse) {
        deleteAddressResponse.value = it
    }

    private fun onErrorDeleteAddress(it: Throwable) {
        errorDeleteAddress.value = it
    }


}