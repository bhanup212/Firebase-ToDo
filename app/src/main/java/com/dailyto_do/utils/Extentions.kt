package com.dailyto_do.utils

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast


fun String.show(context: Context){
    Toast.makeText(context,this,Toast.LENGTH_SHORT).show()
}

fun ProgressBar.isShow(isShow:Boolean){
    this.visibility = if (isShow)View.VISIBLE else View.GONE
}