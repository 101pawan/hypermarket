package com.hypermarket_android.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.hypermarket_android.R

class MyAppsNotificationManager(private var context: Context ? = null,
                                private var notificationManagerCompat: NotificationManagerCompat? =null ,
                                private var notificationManager: NotificationManager? = null  ) {



    companion object{
        private var instance: MyAppsNotificationManager? = null

        fun getInstance(context: Context): MyAppsNotificationManager? {
            if (instance == null) {
                instance = MyAppsNotificationManager(context,
                    NotificationManagerCompat.from(context),
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager  )
            }
            return instance
        }
    }

    fun registerNotificationChannelChannel(
        channelId: String?,
        channelName: String?,
        channelDescription: String?
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = context?.resources?.getString(R.string.CHANNEL_NEWS)
            val descriptionText = context?.resources?.getString(R.string.CHANNEL_DESCRIPTION)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(channelId, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = context?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.description = channelDescription
            val notificationManager = context!!.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
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
        notificationId: Int
    ) {
        val intent = Intent(context, targetNotificationActivity)
        intent.putExtra("count", title)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
//        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context!!, channelId!!)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context!!)
            .setSmallIcon(R.drawable.notification_logo_gray)
           /* .setLargeIcon(
                BitmapFactory.decodeResource(
                    context!!.resources,
                    R.drawable.ic_icon_large
                )
            )*/
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
//            .setChannelId(channelId)
            .setAutoCancel(true)
        val notificationManagerCompat: NotificationManagerCompat =
            NotificationManagerCompat.from(context!!)
        notificationManagerCompat.notify(notificationId, builder.build())
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
        val intent = Intent(context, targetNotificationActivity)
        intent.putExtra("count", title)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, pendingIntentFlag)
//        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context!!, channelId!!)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context!!)
            .setSmallIcon(R.drawable.notification_logo_gray)
           /* .setLargeIcon(
                BitmapFactory.decodeResource(
                    context!!.resources,
                    R.drawable.ic_icon_large
                )
            )*/
            .setContentTitle(title)
            .setContentText(text)
//            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
//            .setChannelId(channelId)
            .setAutoCancel(true)
        notificationManagerCompat?.notify(notificationId, builder.build())
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
        val intent = Intent(context, targetNotificationActivity)
        val taskStackBuilder = TaskStackBuilder.create(context)
        taskStackBuilder.addNextIntentWithParentStack(intent)
        intent.putExtra("count", title)
        val pendingIntent = taskStackBuilder.getPendingIntent(0, pendingIntentFlag)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context!!, channelId!!)
//        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context!!)
            .setSmallIcon(R.drawable.notification_logo_gray)
           /* .setLargeIcon(
                BitmapFactory.decodeResource(
                    context!!.resources,
                    R.drawable.ic_icon_large
                )
            )*/


            .setContentTitle(title)
            .setContentText(text)
//            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setChannelId(channelId!!)
            .setOngoing(false)
            .setAutoCancel(true)
        notificationManagerCompat?.notify(notificationId, builder.build())
    }

    fun updateWithPicture(
        targetNotificationActivity: Class<*>?,
        title: String?,
        text: String?,
        channelId: String?,
        notificationId: Int,
        bigpictureString: String?,
        pendingIntentflag: Int
    ) {
        val intent = Intent(context, targetNotificationActivity)
        intent.putExtra("count", title)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, pendingIntentflag)
//        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context!!, channelId!!)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context!!)
            .setSmallIcon(R.drawable.notification_logo_gray)
            /*.setLargeIcon(
                BitmapFactory.decodeResource(
                    context!!.resources,
                    R.drawable.ic_icon_large
                )
            )*/
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
//            .setChannelId(channelId)
            .setAutoCancel(true)
        val androidImage = BitmapFactory.decodeResource(context!!.resources, R.drawable.big_pic)
        builder.setStyle(
            NotificationCompat.BigPictureStyle().bigPicture(androidImage).setBigContentTitle(bigpictureString)
        )
        notificationManager?.notify(notificationId, builder.build())
    }

    fun cancelNotification(notificationId: Int) {
        notificationManager?.cancel(notificationId)
    }


}