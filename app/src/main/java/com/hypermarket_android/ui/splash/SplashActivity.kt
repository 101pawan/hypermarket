package com.hypermarket_android.ui.splash

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hypermarket_android.R
import com.hypermarket_android.activity.HomeDeleveryActivity
import com.hypermarket_android.ui.HomeActivity
import com.hypermarket_android.ui.login.LoginActivity
import com.hypermarket_android.ui.selectCountry.SelectCountryActivity
import com.hypermarket_android.util.SharedPreferenceUtil
import java.util.*


class SplashActivity : AppCompatActivity() {
    lateinit var sharedPreference: SharedPreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPreference = SharedPreferenceUtil.getInstance(this)



        Log.d("token==",sharedPreference.accessToken)
        Log.d("storeId==",sharedPreference.storeId.toString())
        Log.d("name==",sharedPreference.currentCountry)


        execute()

        if(sharedPreference.language != null){
            if(sharedPreference.language == "en"){
                setLocale("en")
            } else if(sharedPreference.language == "ar"){
                setLocale("ar")
            }else{
                setLocale("en")
            }
        }


//        startActivity(Intent(this, ProductDetailActivity::class.java))

    }




    private fun execute() {

        Handler().postDelayed({
            when {
                sharedPreference.loginStatus -> {
                    Log.e("CurrentActivity",sharedPreference.loginStatus.toString())
                    if (sharedPreference.isDeliveryBoy){
                        startActivity(Intent(this, HomeDeleveryActivity::class.java))
                    }else{
                        startActivity(Intent(this, HomeActivity::class.java))
                    }

                /*    if (sharedPreference.storeId != 0) {
                        startActivity(Intent(this, HomeActivity::class.java))

                    } else {
                        startActivity(Intent(this, ChoosePreferredStoreActivity::class.java))

                    }*/
                }
                sharedPreference.loginStatusSocial -> {
                    Log.e("CurrentActivity1",sharedPreference.loginStatusSocial.toString())
                    if (sharedPreference.isDeliveryBoy){
                        startActivity(Intent(this, HomeDeleveryActivity::class.java))
                    }else{
                        startActivity(Intent(this, HomeActivity::class.java))
                    }


                    /* if (sharedPreference.storeId != 0) {
                         startActivity(Intent(this, HomeActivity::class.java))

                     } else {
                         startActivity(Intent(this, ChoosePreferredStoreActivity::class.java))

                     }*/
                }
                sharedPreference.firstOpen -> {
                    Log.e("CurrentActivity2",sharedPreference.firstOpen.toString())
                    startActivity(Intent(this, SelectCountryActivity::class.java))
                }
                sharedPreference.loginguest -> {
                    Log.e("CurrentActivity3",sharedPreference.loginguest.toString())
                    startActivity(Intent(this, HomeActivity::class.java))

                    /* if (sharedPreference.storeId != 0) {
                        startActivity(Intent(this, HomeActivity::class.java))

                    } else {
                        startActivity(Intent(this, ChoosePreferredStoreActivity::class.java))

                    }*/
                }
                else -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
            finish()
        }, 3000)

    }


    private fun setLocale(lang: String) {
        val locale = Locale(lang)

        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}