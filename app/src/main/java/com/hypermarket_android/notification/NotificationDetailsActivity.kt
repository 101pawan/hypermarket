package com.hypermarket_android.notification

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hypermarket_android.R

class NotificationDetailsActivity : AppCompatActivity() {

    var textViewNotificationDetails: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_details)
        textViewNotificationDetails = findViewById(R.id.textViewNotificationDetails) as TextView
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        textViewNotificationDetails!!.text = intent.getStringExtra("count")
    }
}