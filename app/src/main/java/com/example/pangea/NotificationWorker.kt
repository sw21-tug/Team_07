package com.example.pangea

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.widget.RemoteViews
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(private val context : Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    lateinit var builder: Notification.Builder
    var channelId : String = "i.pangea.notifications"
    override fun doWork(): Result {
        showNotification()
        return Result.success()

    }

    fun showNotification(){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, RegisterAndLoginActivity::class.java)

        // FLAG_UPDATE_CURRENT specifies that if a previous
        // PendingIntent already exists, then the current one
        // will update it with the latest intent
        // 0 is the request code, using it later with the
        // same method again will get back the same pending
        // intent for future reference
        // intent passed here is to our afterNotification class
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // RemoteViews are used to use the content of
        // some different layout apart from the current activity layout
        val contentView = RemoteViews(context.packageName, R.layout.activity_register_login)

        // checking if android version is greater than oreo(API 26) or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, "description", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(context, channelId)

                    .setSmallIcon(R.drawable.ic_baseline_info_24)
                    .setContentTitle("We miss you!")
                    .setContentText("Have not seen you for a while!")
                    //.setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_baseline_info_24))
                    .setContentIntent(pendingIntent)
        } else {

            builder = Notification.Builder(context)

                    .setSmallIcon(R.drawable.ic_baseline_info_24)
                    .setContentTitle("We miss you!")
                    .setContentText("Have not seen you for a while!")
                    //.setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_baseline_info_24))
                    .setContentIntent(pendingIntent)
        }
        notificationManager.notify(69, builder.build())
    }
}