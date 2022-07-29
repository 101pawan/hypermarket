package com.hypermarket_android.util

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.hypermarket_android.R
import com.hypermarket_android.ui.login.LoginActivity
import com.facebook.login.LoginManager
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import retrofit2.HttpException


object ErrorUtil {
    val TAG = ErrorUtil::class.simpleName

    fun handlerGeneralError(context: Context?, throwable: Throwable) {
        Log.e(TAG, "Error: ${throwable.message}")
        throwable.printStackTrace()

        if (context == null) return

        when (throwable) {
            //For Display Toast

            is ConnectException -> Toast.makeText(
                context,
                "Network Error PLease Try Later ",
                Toast.LENGTH_SHORT
            ).show()
            is SocketTimeoutException -> Toast.makeText(
                context,
                "Connection Lost PLease Try Later",
                Toast.LENGTH_SHORT
            ).show()
            is UnknownHostException,is InternalError -> Toast.makeText(
                context,
                "Server Error PLease Try Later",
                Toast.LENGTH_SHORT
            ).show()

            is HttpException -> {
                try {
                    when (throwable.code()) {
                        401 -> {
                            //Logout
                            forceLogout(context)
                        }
                        403 -> {

                            displayError(context, throwable)
                        }
                        else -> {

                            displayError(context, throwable)
                        }
                    }
                } catch (exception: Exception) {
                    Log.e("error",exception.toString())
                }
            }
            else -> {
                Toast.makeText(
                    context, context.resources.getString(R.string.something_went_wrong),
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }


//    ** Perform logout for both the success and error case (force logout)

    private fun forceLogout(context: Context) {

        /*      SharedPreferenceUtil.getInstance(context).deletePreferences()
            context.apply {
            startActivity(Intent(context,MainActivity::class.java)
                .putExtra("IsTokenExpired",true))
        }*/
        Toast.makeText(context, "Please Login or Sign up again ", Toast.LENGTH_SHORT).show()

       /* SharedPreferenceUtil.getInstance(context).loginStatus = false
        SharedPreferenceUtil.getInstance(context).loginStatusSocial = false
        SharedPreferenceUtil.getInstance(context).loginguest = false
        SharedPreferenceUtil.getInstance(context).accessToken = ""*/
        LoginManager.getInstance().logOut()
        if (SharedPreferenceUtil.getInstance(context).loginguest == true) {
            SharedPreferenceUtil.getInstance(context).loginguest = false
        }
        if (SharedPreferenceUtil.getInstance(context).loginStatusSocial == true) {
            SharedPreferenceUtil.getInstance(context).accessToken = ""
            SharedPreferenceUtil.getInstance(context).loginStatusSocial = false
            SharedPreferenceUtil.getInstance(context).full_name = ""
            SharedPreferenceUtil.getInstance(context).email = ""
            SharedPreferenceUtil.getInstance(context).profile_image = ""
        }
        if (SharedPreferenceUtil.getInstance(context).loginStatus == true) {
            SharedPreferenceUtil.getInstance(context).accessToken = ""
            SharedPreferenceUtil.getInstance(context).loginStatus = false
            SharedPreferenceUtil.getInstance(context).full_name = ""
            SharedPreferenceUtil.getInstance(context).email = ""
            SharedPreferenceUtil.getInstance(context).mobile_no = ""
            SharedPreferenceUtil.getInstance(context).profile_image = ""
        }
        SharedPreferenceUtil.getInstance(context).referralCode = ""
       // showToast(context,"Unauthorized:You are already login with other device.")
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)


    }

    fun displayError(context: Context, exception: HttpException) {
        Log.i(TAG, "displayError()")
        try {
            val errorBody = getGsonInstance().fromJson(
                exception.response()!!.errorBody()?.charStream(),
                ErrorBean::class.java
            )
            //  SnackbarUtils.displaySnackbar(view, errorBody.message)
            Log.e("ErrorMessage", errorBody.message)
            Toast.makeText(context, errorBody.message, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("MyExceptions", e.message!!)
            Toast.makeText(
                context, context.getString(R.string.error_exception),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

