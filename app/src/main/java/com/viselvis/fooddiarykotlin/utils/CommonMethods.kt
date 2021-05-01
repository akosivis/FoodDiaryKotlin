package com.viselvis.fooddiarykotlin.utils

import java.util.*

class CommonMethods {

    public fun convertMillisToRealTime(timestamp: Long) : Calendar {
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return calendar
    }
}