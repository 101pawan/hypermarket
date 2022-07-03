package com.hypermarket_android.ui.forgotPassword

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.hypermarket_android.base.BaseViewModel
import com.hypermarket_android.util.ErrorBean
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ForgotViewModel : BaseViewModel() {

    var mResponseForgot = MutableLiveData<ResponseForgot>()
    var mError = MutableLiveData<Throwable>()

    fun getForgetPassword(mobile_number: String, context: Context) {

        apiInterface.forgetPassword(mobile_number).enqueue(object : Callback<ResponseForgot> {
            override fun onFailure(call: Call<ResponseForgot>, t: Throwable) {
                onErrorForgetPassword(t)
            }

            override fun onResponse(
                call: Call<ResponseForgot>, response: Response<ResponseForgot>
            ) {
                if (response.isSuccessful) {
                    onSuccessForgetPassword(response.body())
                } else {
                    onSuccessForgetPassword(null)
                    val gson = GsonBuilder().create()
                    try {
                        val mError = gson.fromJson(response.errorBody()!!.string(), ErrorBean::class.java)
                        Toast.makeText(context, mError.message, Toast.LENGTH_LONG).show()

//                        if (mError.message=="Mobile number/email id  does not exist!"){
//                            Toast.makeText(context, "Please enter valid phone number." +
//                                    "ie. for UAE +9664656788", Toast.LENGTH_LONG).show()
//
//                        }else{
//                            Toast.makeText(context, mError.message, Toast.LENGTH_LONG).show()
//                        }
                    } catch (e: IOException) {

                    }
                }

            }

        })


        /*   apiInterface.forgetPassword(
            mobile_number = mobile_number)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //mProgess.value = false
                onSuccessForgetPassword(it)},{
                //mProgess.value = false
                onErrorForgetPassword(it)})*/

    }

    private fun onErrorForgetPassword(it: Throwable?) {
        mError.value = it
    }

    private fun onSuccessForgetPassword(it: ResponseForgot?) {
        mResponseForgot.value = it
    }

}