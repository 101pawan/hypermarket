package com.hypermarket_android.ui.resetPassword

import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ResetPasswordViewModel:BaseViewModel() {

    var mResponseResetPassword = MutableLiveData<ResponseResetPassword>()
    var mError = MutableLiveData<Throwable>()

    fun getResetPassword(user_id:String,password:String){

        apiInterface.resetPassword(
            user_id = user_id,
            password = password
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //mProgess.value = false
                onSuccessResetPassword(it)},{
                //mProgess.value = false
                onErrorResetPassword(it)})

    }

    private fun onErrorResetPassword(it: Throwable?) {
        mError.value = it
    }

    private fun onSuccessResetPassword(it: ResponseResetPassword?) {
        mResponseResetPassword.value = it
    }

}