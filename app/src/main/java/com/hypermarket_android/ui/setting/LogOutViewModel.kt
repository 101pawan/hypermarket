package com.hypermarket_android.ui.setting

import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LogOutViewModel:BaseViewModel() {
    var mResponseLogOut = MutableLiveData<ResponseLogOut>()
    var mError = MutableLiveData<Throwable>()

    fun getLogOut(accessToken:String){

        apiInterface.getLogOut(
            accessToken = accessToken
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //mProgess.value = false
                onSuccessLogOut(it)},{
                //mProgess.value = false
                onErrorLogOut(it)})

    }

    private fun onErrorLogOut(it: Throwable?) {
        mError.value = it
    }

    private fun onSuccessLogOut(it: ResponseLogOut?) {
        mResponseLogOut.value = it
    }

}