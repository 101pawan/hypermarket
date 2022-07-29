package com.hypermarket_android.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPreferenceUtil
private constructor(val context: Context) {
    val TAG = SharedPreferenceUtil::class.java.simpleName

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("abcde", Context.MODE_PRIVATE)
    /* private val persistableSharedPreferences: SharedPreferences = context.getSharedPreferences(Constant.PERSISTABLE_PREFRENCE_NAME, Context.MODE_PRIVATE)*/

    private val editor: SharedPreferences.Editor = sharedPreferences.edit()
    /* private val persistableEditor: SharedPreferences.Editor = persistableSharedPreferences.edit()*/

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: SharedPreferenceUtil? = null

        fun getInstance(ctx: Context): SharedPreferenceUtil {
            if (instance == null) {
                instance = SharedPreferenceUtil(ctx)
            }
            return instance!!
        }
    }

    var storeName: String
        get() = sharedPreferences["storeName", ""]!!
        set(value) = sharedPreferences.set("storeName", value)

    var country: String
        get() = sharedPreferences["country", ""]!!
        set(value) = sharedPreferences.set("country", value)

    var city: String
        get() = sharedPreferences["city", ""]!!
        set(value) = sharedPreferences.set("city", value)

    var currentCountry: String
        get() = sharedPreferences["currentCountry", ""]!!
        set(value) = sharedPreferences.set("currentCountry", value)

    var storeId: Int
        get() = sharedPreferences["storeId", 0]!!
        set(value) = sharedPreferences.set("storeId", value)


    var loginStatus: Boolean
        get() = sharedPreferences["loginStatus", false]!!
        set(value) = sharedPreferences.set("loginStatus", value)

    var loginStatusSocial: Boolean
        get() = sharedPreferences["loginStatusSocial", false]!!
        set(value) = sharedPreferences.set("loginStatusSocial", value)

    var firstOpen: Boolean
        get() = sharedPreferences["firstOpen", true]!!
        set(value) = sharedPreferences.set("firstOpen", value)

    var accessToken: String
        get() = sharedPreferences["accessToken", ""]!!
        set(value) = sharedPreferences.set("accessToken", value)
    var deliveryBoy: String
        get() = sharedPreferences["deliveryBoy", ""]!!
        set(value) = sharedPreferences.set("deliveryBoy", value)
    var isDeliveryBoy: Boolean
        get() = sharedPreferences["isDeliveryBoy", false]!!
        set(value) = sharedPreferences.set("isDeliveryBoy", value)

    var referralCode: String
        get() = sharedPreferences["referralCode", ""]!!
        set(value) = sharedPreferences.set("referralCode", value)

    var userId: String
        get() = sharedPreferences["userId", ""]!!
        set(value) = sharedPreferences.set("userId", value)

    var profileImage: String
        get() = sharedPreferences["profileImage", "null"]!!
        set(value) = sharedPreferences.set("profileImage", value)

    var profile_image: String
        get() = sharedPreferences["profile_image", ""]!!
        set(value) = sharedPreferences.set("profile_image", value)

    var full_name: String
        get() = sharedPreferences["full_name", ""]!!
        set(value) = sharedPreferences.set("full_name", value)

    var email: String
        get() = sharedPreferences["email", ""]!!
        set(value) = sharedPreferences.set("email", value)

    var mobile_no: String
        get() = sharedPreferences["mobile_no", ""]!!
        set(value) = sharedPreferences.set("mobile_no", value)

    var language: String
        get() = sharedPreferences["language", ""]!!
        set(value) = sharedPreferences.set("language", value)

    var forgot: String
        get() = sharedPreferences["forgot", ""]!!
        set(value) = sharedPreferences.set("forgot", value)

    var country_code: String
        get() = sharedPreferences["country_code", ""]!!
        set(value) = sharedPreferences.set("country_code", value)

    var loginguest: Boolean
        get() = sharedPreferences["loginGuest", false]!!
        set(value) = sharedPreferences.set("loginGuest", value)


    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is Int -> edit { it.putInt(key, value) }
            is String? -> edit { it.putString(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> Log.e(TAG, "Setting shared pref failed for key: $key and value: $value ")
        }
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    /**
     * finds value on given key.
     * [T] is the type of value
     * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
     */
    inline operator fun <reified T : Any> SharedPreferences.get(
        key: String,
        defaultValue: T? = null
    ): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented") as Throwable
        }
    }

    fun deletePreferences() {
        editor.clear()
        editor.apply()
    }
}