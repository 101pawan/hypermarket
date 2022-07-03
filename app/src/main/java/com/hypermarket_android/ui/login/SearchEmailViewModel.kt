package com.hypermarket_android.ui.login

import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.ui.login.AutoSearchingEmail.ResponseSearchEmail
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchEmailViewModel :BaseViewModel(){

    var mResponseSearchEmail = MutableLiveData<ResponseSearchEmail>()
    var mError = MutableLiveData<Throwable>()


    fun getEmail(mobile_number:String){
        apiInterface.getEmail(
            mobile_number=mobile_number
        ) .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //mProgess.value = false
                onSuccessSearchEmail(it)},{
                //mProgess.value = false
                onErrorSearchEmail(it)})
    }

    private fun onErrorSearchEmail(it: Throwable?) {
        mError.value = it

    }

    private fun onSuccessSearchEmail(it: ResponseSearchEmail?) {
        mResponseSearchEmail.value = it
    }


}