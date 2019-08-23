package com.dailyto_do.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.dailyto_do.receiver.TodoReminderReceiver


fun setRepeatAlaram(context: Context,task:String,time:Long){
    val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val service = Intent(context, TodoReminderReceiver::class.java)
    service.putExtra("task",task)
    val sender = PendingIntent.getBroadcast(context,0,service,0)

    am.setInexactRepeating(
        AlarmManager.ELAPSED_REALTIME_WAKEUP,
        time,time,sender)
}

fun setAlaramOnce(context: Context,task:String,time:Long){
    val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val service = Intent(context, TodoReminderReceiver::class.java)
    service.putExtra("task",task)
    val sender = PendingIntent.getBroadcast(context,1,service,0)

    am.setExact(
        AlarmManager.ELAPSED_REALTIME_WAKEUP,
        time,sender)
}