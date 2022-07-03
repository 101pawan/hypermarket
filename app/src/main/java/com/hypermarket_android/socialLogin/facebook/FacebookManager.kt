package com.statusapp.socialLogin.facebook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

import java.util.*

class FacebookManager(private val activity: Activity) : FacebookCallback<LoginResult> {

    private val callbackManager: CallbackManager
    private val loginManager: LoginManager?
    private var listener: OnFacebookLoginListener? = null

    private val accessTokenTracker: AccessTokenTracker
    private var currentToken: AccessToken? = null


    init {
        FacebookSdk.sdkInitialize((activity).applicationContext)

        callbackManager = CallbackManager.Factory.create()
        loginManager = LoginManager.getInstance()
        loginManager?.registerCallback(callbackManager, this)

        accessTokenTracker = object : AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(
                oldAccessToken: AccessToken?,
                currentAccessToken: AccessToken?
            ) {
                if (currentAccessToken != null) {
                    currentToken = currentAccessToken
                }
            }
        }
        currentToken = AccessToken.getCurrentAccessToken()
    }

    fun doLogin() {
        if (loginManager != null) {
            val isLoggedIn = currentToken != null && !currentToken!!.isExpired
            if (isLoggedIn) {
                fetchProfileDetail(currentToken!!)

            } else {
                loginManager.logInWithReadPermissions(
                    activity,
                    Arrays.asList("public_ profile", "email")
                )
            }
        } else {
            if (listener != null) {
                listener!!.onFacebookError("Login manager is not initialized")
            }
        }
    }

    fun doLogout() {
        if (loginManager != null) {
            loginManager.logOut()
        } else {
            if (listener != null) {
                listener!!.onFacebookError("You must login first")
            }
        }
    }

    fun setOnLoginListener(listener: OnFacebookLoginListener) {
        this.listener = listener
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSuccess(result: LoginResult) {
        fetchProfileDetail(result.accessToken)

    }

    private fun fetchProfileDetail(fbAccessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(
            fbAccessToken) { `object`, response ->
            try {

                var picURL = ""
                val jsonObject = response.jsonObject
                jsonObject.optString("name")
                jsonObject.optString("id")
                jsonObject.optString("email")
                if (jsonObject.has("picture")) {

                    //{"data":{"url":"https:\/\/fbcdn-profile-a.akamaihd.net\/hprofile-ak-xpl1\/v\/t1.0-1\/p320x320\/13233156_1056689067736401_8875841216895027796_n.jpg?oh=e0a2202a403e51aeea7b6c078f6cd227&oe=57E63A60&__gda__=1473889794_fbe61a61e4c2649f200e55f591a186bd","is_silhouette":false,"width":320,"height":320}}

                    val picture = jsonObject.getJSONObject("picture")
                    val picData = picture.getJSONObject("data")
                    Log.e("pic", "" + picture.toString())
                    picURL = picData.getString("url")


                }
                var gender = "-1"
                if (jsonObject.optString("gender") == "male") {
                    gender = "1"
                } else if (jsonObject.optString("gender") == "female") {
                    gender = "0"
                }


                if (listener != null) {
                    listener!!.onFacebookLogin(
                        jsonObject.optString("name"),
                        jsonObject.optString("email"),
                        jsonObject.optString("id"),
                        picURL,
                        gender
                    )
                } else {
                    listener!!.onFacebookError("Error Occured")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (e.localizedMessage != null)
                    listener!!.onFacebookError(e.localizedMessage!!)
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "id,email,name,picture.height(300).width(300),gender")
        request.parameters = parameters
        request.executeAsync()
    }

    /*public ArrayList<ContactBean> getFbFriendList(){
           return fbFriendList;
       }*/


    fun getUserPic(userID: String): String {
        return "https://graph.facebook.com/$userID/picture?type=normal"
    }

    override fun onCancel() {

        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut()
            doLogin()
        }

        (activity).runOnUiThread(object : Runnable {
            override fun run() {
                //Toast.makeText(activity, "Facebook login was canceled", Toast.LENGTH_SHORT).show();
                //listener.onFacebookError("Login cancelled");
                Toast.makeText(activity, "Login cancelled", Toast.LENGTH_SHORT).show()

            }
        })
    }

    override fun onError(error: FacebookException) {
        if (error is com.facebook.FacebookAuthorizationException) {
            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut()
                doLogin()
            }
        } else {
            (activity).runOnUiThread(object : Runnable {
                override fun run() {
                    error.printStackTrace()
                    Toast.makeText(activity, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            })
        }
    }

    fun onDestroy() {
        accessTokenTracker.startTracking()
    }

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */

    /*fun logSentFriendRequestEvent() {
        logger.logEvent("sentFriendRequest")
    }*/

}