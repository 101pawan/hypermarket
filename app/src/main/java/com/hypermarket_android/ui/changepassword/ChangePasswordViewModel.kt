package com.hypermarket_android.ui.changepassword

import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.ui.feedback.FeedbackResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ChangePasswordViewModel : BaseViewModel() {

    private lateinit var mDisposable: Disposable

    var changePasswordResponse = MutableLiveData<ChangePasswordResponse>()
    var errorChangePassword = MutableLiveData<Throwable>()
    var mProgessChangePassword = MutableLiveData<Boolean>()
    //    new address api
    fun hitChangePasswordApi(accessToken: String, current_password: String,new_password:String) {

        mDisposable = apiInterface.change_password(
            accessToken = accessToken,
            current_password = current_password,
            new_password = new_password

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mProgessChangePassword.value = true
            }.doOnTerminate {
                mProgessChangePassword.value = false
            }
            .subscribe({
                onSuccessSendMessage(it)
            },
                {
                    onErrorSupport(it)
                })
    }

    private fun onSuccessSendMessage(it: ChangePasswordResponse) {
        changePasswordResponse.value = it
    }

    private fun onErrorSupport(it: Throwable) {
        errorChangePassword.value = it
    }

}