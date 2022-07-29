package com.hypermarket_android

import android.app.Application
import android.content.res.Resources
import android.util.Log
import com.hypermarket_android.notification.MyAppsNotificationManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

class MyApplication : Application() {
    var myAppsNotificationManager: MyAppsNotificationManager? = null
    override fun onCreate() {
        super.onCreate()
        myAppsNotificationManager = MyAppsNotificationManager.getInstance(this)
        myAppsNotificationManager?.registerNotificationChannelChannel(
            getString(R.string.NEWS_CHANNEL_ID),
            getString(R.string.CHANNEL_NEWS),
            getString(R.string.CHANNEL_DESCRIPTION)
        )
        FirebaseMessaging.getInstance().isAutoInitEnabled

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isComplete) {
                Log.i(getString(R.string.DEBUG_TAG), " Task Filed")
                return@OnCompleteListener
            }
//            Log.d(
//                getString(R.string.DEBUG_TAG), " The completed result: " + task?.result?.token
//            )
            //Making an API call - Thread, Volley, okHttp, Retrofit
        })
    }
    companion object {
        fun getScreenWidth(): Int {
            return Resources.getSystem().displayMetrics.widthPixels
        }
    }


    fun triggerNotification(
        targetNotificationActivity: Class<*>?,
        channelId: String?,
        title: String?,
        text: String?,
        bigText: String?,
        priority: Int,
        autoCancel: Boolean,
        notificationId: Int,
        pendingIntentFlag: Int
    ) {
        myAppsNotificationManager!!.triggerNotification(
            targetNotificationActivity,
            channelId,
            title,
            text,
            bigText,
            priority,
            autoCancel,
            notificationId,
            pendingIntentFlag
        )
    }

    fun triggerNotification(
        targetNotificationActivity: Class<*>?,
        channelId: String?,
        title: String?,
        text: String?,
        bigText: String?,
        priority: Int,
        autoCancel: Boolean,
        notificationId: Int
    ) {
        myAppsNotificationManager!!.triggerNotification(
            targetNotificationActivity,
            channelId,
            title,
            text,
            bigText,
            priority,
            autoCancel,
            notificationId
        )
    }

    fun triggerNotificationWithBackStack(
        targetNotificationActivity: Class<*>?,
        channelId: String?,
        title: String?,
        text: String?,
        bigText: String?,
        priority: Int,
        autoCancel: Boolean,
        notificationId: Int,
        pendingIntentFlag: Int
    ) {
        myAppsNotificationManager!!.triggerNotificationWithBackStack(
            targetNotificationActivity,
            channelId,
            title,
            text,
            bigText,
            priority,
            autoCancel,
            notificationId,
            pendingIntentFlag
        )
    }

    fun updateNotification(
        targetNotificationActivity: Class<*>?,
        title: String?,
        text: String?,
        channelId: String?,
        notificationId: Int,
        bigpictureString: String?,
        pendingIntentflag: Int
    ) {
        myAppsNotificationManager!!.updateWithPicture(
            targetNotificationActivity,
            title,
            text,
            channelId,
            notificationId,
            bigpictureString,
            pendingIntentflag
        )
    }

    fun cancelNotification(notificaitonId: Int) {
        myAppsNotificationManager!!.cancelNotification(notificaitonId)
    }
}