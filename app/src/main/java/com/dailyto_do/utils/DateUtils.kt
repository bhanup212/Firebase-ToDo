package com.dailyto_do.utils

import java.text.SimpleDateFormat
import java.util.*

const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"


fun getDateFormat() = SimpleDateFormat(DATE_FORMAT)

fun getCurrentDate():String{
    val sdf = SimpleDateFormat(DATE_FORMAT)
    val date = sdf.format(Date())
    return date.toString()
}

fun getCurrentTimeMills():Long{
    val date = getDateFormat().parse(getCurrentDate())
    val calender = Calendar.getInstance()
    calender.time = date
    return calender.timeInMillis
}