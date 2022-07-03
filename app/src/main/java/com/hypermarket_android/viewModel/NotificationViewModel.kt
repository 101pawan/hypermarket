package com.hypermarket_android.viewModel

import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.dataModel.NotificationResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class NotificationViewModel : BaseViewModel() {
    private lateinit var mDisposable: Disposable

    var notificationResponse = MutableLiveData<NotificationResponse>()
    var errorNotification = MutableLiveData<Throwable>()
    var mProgessNotification = MutableLiveData<Boolean>()



    //    new address api
    fun hitNotificationApi(accessToken: String
    ) {
        mDisposable = apiInterface.getNotification(
            accessToken = accessToken

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mProgessNotification.value = true
            }.doOnTerminate {
                mProgessNotification.value = false
            }
            .subscribe({
                onSuccessNotification(it)
            },
                {
                    onErrorNotification(it)
                })
    }

    private fun onSuccessNotification(it: NotificationResponse) {
        notificationResponse.value = it
    }

    private fun onErrorNotification(it: Throwable) {
        errorNotification.value = it
    }






}