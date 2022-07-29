package com.hypermarket_android.viewModel

import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.dataModel.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AddRatingViewModel : BaseViewModel() {

    private lateinit var mDisposable: Disposable
    var addRatingResponse = MutableLiveData<AddRatingResponse>()
    var errorRatingResponse = MutableLiveData<Throwable>()

    fun addRating(
        accessToken: String,
        user_id: String,
        product_id: String,
        rating: String,
        review: String
    ) {
        mDisposable = apiInterface.addRating(
            accessToken = accessToken,
            user_id = user_id,
            product_id = product_id,
            rating = rating,
            review = review

        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessAddReview(it)
            },
                {
                    onErrorAddOrder(it)
                })
    }

    private fun onSuccessAddReview(it: AddRatingResponse) {
        addRatingResponse.value = it
    }

    private fun onErrorAddOrder(it: Throwable) {
        errorRatingResponse.value = it
    }


}