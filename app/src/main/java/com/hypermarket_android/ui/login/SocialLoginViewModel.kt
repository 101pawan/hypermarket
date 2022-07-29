package com.hypermarket_android.ui.login

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.ui.register.socialSignUp.ResponseSocialSignUp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SocialLoginViewModel :BaseViewModel(){

    var mResponseSocialSignUp =MutableLiveData<ResponseSocialSignUp>()
    var mError = MutableLiveData<Throwable>()

    @SuppressLint("CheckResult")
    fun getSocialSignUp(email: String, device_token:String, device_type:String, social_id:String, social_type:String, name:String, image:String,language:String){
        apiInterface.socialSignUp(
            email=email,
            device_token=device_token,
            device_type=device_type,
            social_id=social_id,
            social_type=social_type,
            name=name,
            image=image,
            language = language
        ) .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //mProgess.value = false
                onSuccessSocialSignUp(it)},{
                //mProgess.value = false
                onErrorSignUp(it)})
    }

    private fun onSuccessSocialSignUp(it: ResponseSocialSignUp?) {
        mResponseSocialSignUp.value=it
    }

    private fun onErrorSignUp(it: Throwable?) {
        mError.value=it
    }

}