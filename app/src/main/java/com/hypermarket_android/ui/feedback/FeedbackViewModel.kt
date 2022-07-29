package com.hypermarket_android.ui.feedback

import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.dataModel.SendMessageResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FeedbackViewModel : BaseViewModel() {

    private lateinit var mDisposable: Disposable

    var feedbackResponse = MutableLiveData<FeedbackResponse>()
    var errorFeedback = MutableLiveData<Throwable>()
    var mProgessFeedback = MutableLiveData<Boolean>()
    //    new address api
    fun hitFeedbackApi(accessToken: String, message: String) {

        mDisposable = apiInterface.feedback(
            accessToken = accessToken,

            message = message

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mProgessFeedback.value = true
            }.doOnTerminate {
                mProgessFeedback.value = false
            }
            .subscribe({
                onSuccessSendMessage(it)
            },
                {
                    onErrorSupport(it)
                })
    }

    private fun onSuccessSendMessage(it: FeedbackResponse) {
        feedbackResponse.value = it
    }

    private fun onErrorSupport(it: Throwable) {
        errorFeedback.value = it
    }

}