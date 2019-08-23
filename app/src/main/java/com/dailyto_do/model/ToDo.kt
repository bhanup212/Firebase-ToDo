package com.dailyto_do.model

data class ToDo(
    var id:String="",
    var name:String="",
    var date:String="",
    var isCompleted:Boolean=false,
    var isDailyReminder:Boolean=false,
    var isOneTimeReminder:Boolean=false,
    var reminderTime:String=""
)