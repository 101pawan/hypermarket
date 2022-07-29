package com.hypermarket_android.viewModel

import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.dataModel.SendMessageResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SendMessageViewModel :BaseViewModel() {
    private lateinit var mDisposable: Disposable

    var sendMessageResponse = MutableLiveData<SendMessageResponse>()
    var errorSendMessage = MutableLiveData<Throwable>()
    var mProgessSendMessage = MutableLiveData<Boolean>()
    //    new address api
    fun hitSendMessageApi(accessToken: String, fullName: String,countryCode: String,
                          mobile: String,
                          email: String, about: String,
                          message: String
    ) {

        mDisposable = apiInterface.sendMessage(
            accessToken = accessToken,
            fullName = fullName,
            countryCode =countryCode,
            mobile = mobile,
            email = email,
            about = about,
            message = message

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mProgessSendMessage.value = true
            }.doOnTerminate {
                mProgessSendMessage.value = false
            }
            .subscribe({
                onSuccessSendMessage(it)
            },
                {
                    onErrorSupport(it)
                })
    }

    private fun onSuccessSendMessage(it: SendMessageResponse  ) {
        sendMessageResponse.value = it
    }

    private fun onErrorSupport(it: Throwable) {
        errorSendMessage.value = it
    }




}