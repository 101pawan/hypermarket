package com.hypermarket_android.util

import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.hypermarket_android.R
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

object SnackbarUtils {
    @Suppress("unused")
    val TAG = SnackbarUtils::class.simpleName

    fun displaySnackbar(view: View?, message: String?, duration: Int = Snackbar.LENGTH_SHORT) {
        if (view == null) return
        if (message == null) {
            somethingWentWrong(view)
            return
        }
        val snackbar = Snackbar.make(view, message, duration)
        val tv = snackbar.view.findViewById(R.id.snackbar_text) as TextView
        val params = snackbar.view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.CENTER_VERTICAL
        tv.setTextColor(Color.WHITE)
        snackbar.view
        snackbar.show()
    }

    fun somethingWentWrong(view: View?) {
        if (view == null) return
        //displaySnackbar(view, view.context.getString(R.string.error_something_went_wrong_please_retry))
        Toast.makeText(
            view.context,
            view.context.getString(R.string.error_something_went_wrong_please_retry),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun displayError(view: View?, socketTimeoutException: SocketTimeoutException) {
        if (view == null) return
        //displaySnackbar(view, view.context.getString(R.string.error_connection_please_check_internet))
        Toast.makeText(
            view.context,
            view.context.getString(R.string.no_internet_connection),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun displayError(view: View?, connectionException: ConnectException) {
        if (view == null) return
        //displaySnackbar(view, view.context.getString(R.string.error_connection_please_check_internet))
        Toast.makeText(
            view.context,
            view.context.getString(R.string.no_internet_connection),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun displayError(view: View?, exception: HttpException) {
        Log.i(TAG, "displayError()")
        try {
            GsonUtil.mGsonInstance = Gson()
            val errorBody =
                GsonUtil.mGsonInstance!!.fromJson(
                    exception.response()?.errorBody()?.charStream(),
                    ErrorBean::class.java
                )
            //displaySnackbar(view, errorBody.message)
            Toast.makeText(view!!.context, errorBody.message, Toast.LENGTH_SHORT).show()
            Log.e("ErrorMessage", errorBody.message)
        } catch (e: Exception) {
            somethingWentWrong(view)
        }
    }
}