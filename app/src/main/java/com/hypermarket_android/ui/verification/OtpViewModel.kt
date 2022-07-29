package com.hypermarket_android.ui.verification

import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OtpViewModel:BaseViewModel() {
    var mResponseOtp = MutableLiveData<ResposeOtp>()
    var mResponseResendOtp = MutableLiveData<ResponseResendOtp>()
    var mError = MutableLiveData<Throwable>()

    fun getOtp(user_id:String,otp: String){

        apiInterface.otp(
            user_id = user_id,
            otp = otp
        )

            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //mProgess.value = false
                onSuccessOtp(it)},{
                //mProgess.value = false
                onErrorOtp(it)})
    }

    fun getResendOtp(user_id:String){

        apiInterface.resendOtp(
            user_id = user_id
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //mProgess.value = false
                onSuccessResendOtp(it)},{
                //mProgess.value = false
                onErrorOtp(it)})
    }

    private fun onSuccessResendOtp(it: ResponseResendOtp?) {
        mResponseResendOtp.value = it
    }


    private fun onErrorOtp(it: Throwable?) {
        mError.value=it
    }

    private fun onSuccessOtp(it: ResposeOtp?) {
        mResponseOtp.value=it
    }

}