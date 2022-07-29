package com.hypermarket_android.ui.register

import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RegisterViewModel:BaseViewModel() {

    var mResponseRegister =MutableLiveData<ResponseRegister>()

    var mError = MutableLiveData<Throwable>()

    fun getRegister(name: String,mobile_number:String,email:String,country_name:String,password:String,country_code:String,device_token:String,device_type:String,language:String){
        apiInterface.signup(
            name=name,
            mobile_number=mobile_number,
            email=email,
            country_name=country_name,
            password=password,
            country_code=country_code,
            device_token=device_token,
            device_type=device_type,
            language = language
        ) .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //mProgess.value = false
                onSuccessSignUp(it)},{
                //mProgess.value = false
                onErrorSignUp(it)})
    }




    private fun onErrorSignUp(it: Throwable?) {
        mError.value=it
    }


    private fun onSuccessSignUp(it: ResponseRegister?) {
        mResponseRegister.value=it
    }

}