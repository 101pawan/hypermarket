package com.hypermarket_android.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Build
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


fun isInputValidation(text: String?): Boolean {
    return (text != null) && (text != "")
}

var gson: Gson? = null
fun getGsonInstance(): Gson {

    if (gson == null)
        gson = Gson()
    return gson!!
}

val String.isValidPassword: Boolean
    get() = this.length >= 8

fun EditText.isValidPassword(text: String?): Boolean {
    return (text!!.length <= 7)
}

// To Get String With Trim From View
fun EditText.getString(): String {
    return this.text.toString().trim()
}

// To Show Toast
var toast: Toast? = null

fun AppCompatActivity.showToast(message: String) {
    if (toast != null) toast!!.cancel()
    toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast?.show()
}

fun showToast(context: Context, message: String) {
    if (toast != null) toast!!.cancel()
    toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
    toast?.show()
}

fun convertDate(date: String): String {
    //var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
    var date: Date =
        dateFormat.parse(date) //You will get date object relative to server/client timezone wherever it is parsed

    var formatter: DateFormat =
        SimpleDateFormat("dd MMM yyyy, HH:mm") //If you need time just put specific format for time like 'HH:mm:ss'

    return formatter.format(date)
}

fun convertNewDate(date: String): String {
    //var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    var date: Date =
        dateFormat.parse(date) //You will get date object relative to server/client timezone wherever it is parsed

    var formatter: DateFormat =
        SimpleDateFormat("dd MMM yyyy, HH:mm") //If you need time just put specific format for time like 'HH:mm:ss'

    return formatter.format(date)
}

fun convertExpectedDelivery(date: Long?): String {
    val result = date?.let { Date(it) }
    val dateFormat: DateFormat = SimpleDateFormat("dd-MMM-yyyy")
    return dateFormat.format(result)
}


fun ImageView.setImageFromDevice(fileUri: String?) {
    fileUri?.let {
        val bmp = BitmapFactory.decodeFile(fileUri);
        setImageBitmap(bmp)
    }

}

fun checkStoragePermission(context: Context): Boolean {
    var granted = false
    val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    Permissions.check(context, permissions, null, null, object : PermissionHandler() {
        override fun onGranted() {
            granted = true
        }

        override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
            granted = false
        }
    })
    return granted
}

fun checkCameraPermission(context: Context): Boolean {
    var granted = false
    Permissions.check(context, Manifest.permission.CAMERA, null,
        object : PermissionHandler() {
            override fun onGranted() {
                granted = true
            }

            override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
                granted = false
            }
        })
    return granted
}

fun checkMicePermission(context: Context): Boolean {
    var granted = false
    Permissions.check(context, Manifest.permission.RECORD_AUDIO, null,
        object : PermissionHandler() {
            override fun onGranted() {
                granted = true
            }

            override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
                granted = false
            }
        })
    return granted
}

fun checkLocationPermission(context: Context): Boolean {
    var granted = false
    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    Permissions.check(context, permissions, null, null,
        object : PermissionHandler() {
            override fun onGranted() {
                granted = true
            }

            override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
                granted = false
            }
        })
    return granted
}

//TimeZone
fun getTimeShow(jointDate: String): String {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val past: Date = format.parse(jointDate)!!
    val currentDate = Date()
    val second: Long = TimeUnit.MILLISECONDS.toSeconds(currentDate.time - past.time)
    val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(currentDate.time - past.time)
    val hours: Long = TimeUnit.MILLISECONDS.toHours(currentDate.time - past.time)
    val days: Long = TimeUnit.MILLISECONDS.toDays(currentDate.time - past.time)
    val month: Double = (days.toDouble() / 30)
    val properMonth = DecimalFormat("##.#").format(month)

    when {
        second < 60 -> {
            return "$second second ago"
        }

        minutes < 60 -> {
            return "$minutes minute ago"
        }

        hours < 24 -> {
            return "$hours hour ago"
        }

        days < 30 -> {
            return "$days day ago"
        }

        month < 12 -> {
            return "$properMonth month ago"
        }

        else -> {
            val year: Int = (month / 12).toInt()
            val properYear = DecimalFormat("##.#").format(year)
            return "$properYear year ago"
        }
    }
}

//Network Connection Available
fun isNetworkConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnectedOrConnecting
}

//EditText is Empty
fun EditText.isEditTextEmpty(message: String): Boolean {
    return if (this.getString() == "") {
        this.error = message
        this.requestFocus()
        false
    } else
        true
}

//edit text length
fun EditText.checkEditTextFullNameLength(message: String): Boolean {
    return if (this.getString().length > 36) {
        this.error = message
        this.requestFocus(this.getString().length)
        false
    } else
        true
}


//Check Password Length
fun EditText.checkEditTextLength(message: String): Boolean {
    return if (this.getString().length < 6) {
        this.error = message
        this.requestFocus(this.getString().length)
        false
    } else
        true
}

//Password Matching Function
fun EditText.isPasswordMatch(password: String, message: String): Boolean {
    return if (this.getString() != password) {
        this.error = message
        this.requestFocus(this.getString().length)
        false
    } else
        true
}


/*
const val EMAIL_PATTERN =
    "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

*/


val String.isValidEmail: Boolean
    get() = if (this.length < 3 || this.length > 265)
        false
    else {
        this.matches(Constants.EMAIL_PATTERN.toRegex())
    }

val String.isValidMobile: Boolean
    get() = this.length in 8..15


fun EditText.isValidEmail(message: String): Boolean {
    return if (!Patterns.EMAIL_ADDRESS.matcher(this.getString()).matches()) {
        this.error = message
        this.requestFocus(this.getString().length)
        false
    } else
        true
}

fun showShortToast(msg: String, context: Context?) {
    val toast = Toast.makeText(
        context,
        msg, Toast.LENGTH_SHORT
    )
    toast.show()
}

fun AppCompatActivity.changeStatusBarColor() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val decor = this.window.decorView
        decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = activity.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}