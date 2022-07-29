package com.hypermarket_android.viewModel

import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.dataModel.DeleteAddressResponse
import com.hypermarket_android.dataModel.ManageAddressResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ManageAddressViewModel : BaseViewModel() {
    private lateinit var mDisposable: Disposable
    var manageAddressResponse = MutableLiveData<ManageAddressResponse>()
    var errorManageAddress = MutableLiveData<Throwable>()
    var mProgessManageAddress = MutableLiveData<Boolean>()

    var deleteAddressResponse = MutableLiveData<DeleteAddressResponse>()
    var errorDeleteAddress = MutableLiveData<Throwable>()
    var mProgessDeleteAddress = MutableLiveData<Boolean>()


    //    hitAddNewAddress
    fun hitManageAddressApi(accessToken: String) {
        mDisposable = apiInterface.getMangeAddressList(
            accessToken = accessToken,

            ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mProgessManageAddress.value = true
            }.doOnTerminate {
                mProgessManageAddress.value = false
            }
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
            .doOnSubscribe {
                mProgessDeleteAddress.value = true
            }.doOnTerminate {
                mProgessDeleteAddress.value = false
            }
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