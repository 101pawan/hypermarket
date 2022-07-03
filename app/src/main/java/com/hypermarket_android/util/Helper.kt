package com.hypermarket_android.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import java.io.IOException
import java.util.*


class Helper {

    companion object {
        fun isNetworkAvailable(activity: Activity): Boolean {
            val connectivityManager =
                activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null
        }


        // hiding keyboard on Back
        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManager.isActive) {
                if (activity.currentFocus != null) {
                    inputMethodManager.hideSoftInputFromWindow(
                        activity.currentFocus!!.windowToken,
                        0
                    )
                }
            }
        }

        // Email Validation
        fun isEmailValid(email: String): Boolean {
            return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        /*  // Email Limit Validation
          fun isPasswordValid(password: String): Boolean {
              return password.length >= Constant.PASSWORD_LIMIT
          }*/


        //Getting the permission status: Storage
        fun isReadExternalStoragePermissionAllowed(activity: Activity): Boolean {
            val result = ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            return result == PackageManager.PERMISSION_GRANTED
        }


        @SuppressLint("MissingPermission")
        fun getCurrentLocation(activity: Activity) :String{
            /*val locationManager =
                activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                5000,
                0f,
                locationListener(activity)
            )
            val sharedPreference=SharedPreferenceUtil.getInstance(activity)
            return sharedPreference.currentCountry*/
            return Constants.UNITED_ARAB_EMIRATES
        }

        fun locationListener(activity: Activity): LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val sharedPreference=SharedPreferenceUtil.getInstance(activity)
                try {
                    val gcd = Geocoder(activity, Locale.getDefault())
                    val addresses = gcd.getFromLocation(location.latitude, location.longitude, 1)
                    if (addresses.size > 0)
                        sharedPreference.currentCountry=addresses.get(0).countryName
                     //  val countryName =addresses.get(0).countryName
//                        countryName="Oman"


                    //System.out.println(addresses.get(0).getLocality());
                } catch (e: IOException) {

                    e.printStackTrace()
                }

            }
        }

        @SuppressLint("MissingPermission")
        fun getCurrentLoactionTesting(activity: Activity) :String{
            val locationManager =
                activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
            //   val locationListener = MyLocationListener()

            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                5000,
                0f,
                locationListener(activity)
            )
            val sharedPreference=SharedPreferenceUtil.getInstance(activity)
            return sharedPreference.currentCountry
        }



    }


    private inner class MyLocationListener : LocationListener {

        override fun onLocationChanged(loc: Location) {
        }

        override fun onProviderDisabled(provider: String) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

        }
    }

}
/*
fun AppCompatActivity.statusBarWhite() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.window.statusBarColor = this.resources.getColor(R.color.colorBlack)
        this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}*/
