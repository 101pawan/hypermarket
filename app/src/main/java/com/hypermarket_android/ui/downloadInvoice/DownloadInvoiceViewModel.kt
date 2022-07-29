package com.hypermarket_android.ui.downloadInvoice

import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.ui.changepassword.ChangePasswordResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DownloadInvoiceViewModel : BaseViewModel() {

    private lateinit var mDisposable: Disposable

    var downloadInvoiceResponse = MutableLiveData<DownloadInvoiceResponse>()
    var errorDownloadInvoice = MutableLiveData<Throwable>()
    var mProgessDownloadInvoice = MutableLiveData<Boolean>()
    //    new address api
    fun hitDownloadInvoiceApi(accessToken: String, user_id: String) {

        mDisposable = apiInterface.download_invoice(
            accessToken = accessToken,
            user_id = user_id

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mProgessDownloadInvoice.value = true
            }.doOnTerminate {
                mProgessDownloadInvoice.value = false
            }
            .subscribe({
                onSuccessSendMessage(it)
            },
                {
                    onErrorSupport(it)
                })
    }

    private fun onSuccessSendMessage(it: DownloadInvoiceResponse) {
        downloadInvoiceResponse.value = it
    }

    private fun onErrorSupport(it: Throwable) {
        errorDownloadInvoice.value = it
    }

}