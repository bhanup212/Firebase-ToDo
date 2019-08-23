package com.dailyto_do.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dailyto_do.R

class TodoReminderReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d("Receiver","Todo Reminder Receiver")
        Log.d("Receiver","Todo task is ${p1?.getStringExtra("task")}")
        if (p1 != null){
            showNotification(p0!!,p1.getStringExtra("task"))
        }
    }

    private fun showNotification(context: Context,message: String) {
        val notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = 37

        val builder = NotificationCompat.Builder(context, "To_do")
            .setContentTitle("Reminder")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_beenhere)
            .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            .setContentText(message)
            .setAutoCancel(true)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "todo_notifications"
            val channel = NotificationChannel(channelId, "todo", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "todo broadcast receiver"
            channel.enableLights(true)
            channel.lightColor = Color.GREEN
            channel.setShowBadge(true)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(notificationId, builder.build())

    }
}

