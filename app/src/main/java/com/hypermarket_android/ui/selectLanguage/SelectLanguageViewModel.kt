package com.hypermarket_android.ui.selectLanguage

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.ui.login.ResponseLogin
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SelectLanguageViewModel: BaseViewModel() {
    var mResponseLanguage = MutableLiveData<ResponseLanguage>()

    var mError = MutableLiveData<Throwable>()

    @SuppressLint("CheckResult")
    fun updateLanguage(accessToken:String, userId:String, language:String){
        apiInterface.updateLanguage(
            accessToken,language,userId
        ) .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //mProgess.value = false
                onSuccessLanguage(it)},{
                //mProgess.value = false
                onErrorLanguage(it)})
    }


    private fun onErrorLanguage(it: Throwable?) {
        mError.value=it
    }

    private fun onSuccessLanguage(it: ResponseLanguage?) {
        mResponseLanguage.value=it
    }
}