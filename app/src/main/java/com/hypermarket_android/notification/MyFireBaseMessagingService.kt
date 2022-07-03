package com.hypermarket_android.notification

import android.app.PendingIntent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.hypermarket_android.MyApplication
import com.hypermarket_android.R
import com.hypermarket_android.activity.NotificationYasMartActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFireBaseMessagingService : FirebaseMessagingService() {
    private val TAG = MyFireBaseMessagingService::class.java.simpleName
    internal enum class PUSH_NOTIFICATION_SOURCE {
        CONSOLE, API_WITHOUT_NOTIFICATION, API_WITH_NOTIFICATION, UNKNOWN_SOURCE
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.i(getString(R.string.DEBUG_TAG), "New token: $s")
        //Making an API call - Thread, Volley, okHttp, Retrofit
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val notificationSource = getNotificationSource(remoteMessage)
        Log.i(
            getString(R.string.DEBUG_TAG),
            "Remote Message received from : $notificationSource"
        )
        when (notificationSource) {
            PUSH_NOTIFICATION_SOURCE.CONSOLE -> (application as MyApplication).triggerNotification(
                NotificationYasMartActivity::class.java,
                getString(R.string.NEWS_CHANNEL_ID),
                remoteMessage.notification!!.title,
                remoteMessage.notification!!.body,
                "This notification is from FCM Console ",
                NotificationCompat.PRIORITY_HIGH,
                false,
                resources.getInteger(R.integer.notificationId),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            PUSH_NOTIFICATION_SOURCE.API_WITH_NOTIFICATION -> {
                val data = remoteMessage.data
                (application as MyApplication).triggerNotificationWithBackStack(
                NotificationYasMartActivity::class.java,
                getString(R.string.NEWS_CHANNEL_ID),
                    data["title"],
                    data["body"],
                "This notification is from FCM API call with notification title and body",
                NotificationCompat.PRIORITY_HIGH,
                false,
                resources.getInteger(R.integer.notificationId),
                PendingIntent.FLAG_UPDATE_CURRENT
            )}
            PUSH_NOTIFICATION_SOURCE.API_WITHOUT_NOTIFICATION ->{

                val data = remoteMessage?.data

                (application as MyApplication).triggerNotificationWithBackStack(
                NotificationYasMartActivity::class.java,
                getString(R.string.NEWS_CHANNEL_ID),
                    data["title"],
                    data["body"],
                "This notification is from FCM API call without notification title and body",
                NotificationCompat.PRIORITY_HIGH,
                false,
                resources.getInteger(R.integer.notificationId),
                PendingIntent.FLAG_UPDATE_CURRENT
            )}
            PUSH_NOTIFICATION_SOURCE.UNKNOWN_SOURCE -> Log.i(
                TAG,
                "Since it's unknown source, don't want to do anything"
            )

        }
    }

    private fun getNotificationSource(remoteMessage: RemoteMessage): PUSH_NOTIFICATION_SOURCE {
        val notificationSource: PUSH_NOTIFICATION_SOURCE
        val notification = remoteMessage.notification
        val data = remoteMessage.data
        notificationSource = if (notification != null && data != null) {
            if (data.size == 0) {
                PUSH_NOTIFICATION_SOURCE.CONSOLE
            } else {
                PUSH_NOTIFICATION_SOURCE.API_WITH_NOTIFICATION
            }
        } else if (remoteMessage.data != null) {
            PUSH_NOTIFICATION_SOURCE.API_WITHOUT_NOTIFICATION
        } else {
            PUSH_NOTIFICATION_SOURCE.UNKNOWN_SOURCE
        }
        return notificationSource
    }





}