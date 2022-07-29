package com.hypermarket_android.util

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import java.util.*


class NotifyReceiver(val context: Context) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        for (key in Objects.requireNonNull(intent!!.extras)!!.keySet()) {
            try {
                val componentInfo = intent.extras!![key] as ComponentName?
                val packageManager = context!!.packageManager
                assert(componentInfo != null)
                val appName = packageManager.getApplicationLabel(
                    packageManager.getApplicationInfo(
                        componentInfo!!.packageName, PackageManager.GET_META_DATA
                    )
                ) as String
                Log.e("xxxxxxxxxx====>", appName)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}