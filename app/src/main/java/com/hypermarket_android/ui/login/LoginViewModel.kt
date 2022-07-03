package com.hypermarket_android.ui.login

import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel :BaseViewModel(){

    var mResponseLogin = MutableLiveData<ResponseLogin>()

    var mError = MutableLiveData<Throwable>()


    fun getLogin(mobile_number:String,country_code:String,device_token:String,device_type:String,password:String){
        apiInterface.login(
            mobile_number=mobile_number,
            country_code=country_code,
            device_token=device_token,
            device_type=device_type,
            password=password
        ) .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //mProgess.value = false
                onSuccessLogin(it)},{
                //mProgess.value = false
                onErrorLogin(it)})
    }




    private fun onErrorLogin(it: Throwable?) {
        mError.value=it
    }

    private fun onSuccessLogin(it: ResponseLogin?) {
        mResponseLogin.value=it
    }

}