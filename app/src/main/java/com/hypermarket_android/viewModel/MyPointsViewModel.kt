package com.hypermarket_android.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.dataModel.AddRewardsResposne
import com.hypermarket_android.dataModel.GetEarnResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MyPointsViewModel : BaseViewModel() {

    private lateinit var mDisposable: Disposable
    var earnResponse = MutableLiveData<GetEarnResponse>()
    var addEarnResponse = MutableLiveData<AddRewardsResposne>()
    var errorResponse = MutableLiveData<Throwable>()


    fun getUserRewardPoint(
        accessToken: String,
        user_id: String
    ) {
        mDisposable = apiInterface.getUserRewardPoint(
            accessToken = accessToken,
            user_id = user_id
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccess(it)
            },
                {
                    onError(it)
                })
    }

    private fun onSuccess(it: GetEarnResponse?) {
        earnResponse.value = it
    }

    fun addUserRewardPoints(
        accessToken: String,
        user_id: String
    ) {
        mDisposable = apiInterface.addUserRewardPoints(
            accessToken = accessToken,
            user_id = user_id
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccess(it)
            },
                {
                    onError(it)
                })
    }

    private fun onSuccess(it: AddRewardsResposne?) {
        addEarnResponse.value = it
    }


    private fun onError(it: Throwable) {
        Log.e("Fsfsd", it.message!!)
        errorResponse.value = it
    }


}