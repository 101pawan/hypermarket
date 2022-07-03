package com.hypermarket_android.notification

import android.app.PendingIntent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.hypermarket_android.MyApplication
import com.hypermarket_android.R

class MainNotificationActivity : AppCompatActivity() {

    var buttonTriggerNotification: Button? =null
    var buttonCancel:android.widget.Button? = null
    var buttonUpdate:android.widget.Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_main)
        buttonTriggerNotification = findViewById(R.id.buttonTriggerNotification) as Button
        buttonCancel = findViewById(R.id.buttonCancel) as Button
        buttonUpdate = findViewById(R.id.buttonUpdate) as Button
        buttonTriggerNotification!!.setOnClickListener {
            (application as MyApplication).triggerNotificationWithBackStack(
                NotificationDetailsActivity::class.java,
                getString(R.string.NEWS_CHANNEL_ID),
                "Sample Notification",
                "This is a sample notification app",
                "This is a sample notification created by Codetutor for demonstration of how to trigger notifications in Android app ",
                NotificationCompat.PRIORITY_HIGH,
                true,
                resources.getInteger(R.integer.notificationId),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        buttonCancel!!.setOnClickListener(View.OnClickListener {
            (application as MyApplication).cancelNotification(
                resources.getInteger(R.integer.notificationId)
            )
        })
        buttonUpdate!!.setOnClickListener(View.OnClickListener {
            (application as MyApplication).updateNotification(
                NotificationDetailsActivity::class.java,
                "Updated Notification",
                "This is updatedNotification",
                getString(R.string.NEWS_CHANNEL_ID),
                resources.getInteger(R.integer.notificationId),
                "This is a updated information for bigpicture String",
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        })
    }


}