package com.viselvis.fooddiarykotlin.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

fun convertMillisToRealTime(timestamp: Long) : Calendar {
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp
    return calendar
}
 fun areDatesTheSame(date1: Date?, date2: Date) : Boolean {
     if (date1 == null) return false

     val format = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
     return format.format(date1).equals(format.format(date2))
 }

fun convertDateToText(dateInput: Date, outputType: Int = 0): String {
    val format = SimpleDateFormat( when (outputType) {
        1 -> "HH:mm"
        else -> "MMM dd, yyyy" }, Locale.getDefault())
    return format.format(dateInput)
}

fun convertDateToFileName(dateInput: Date): String {
    val format = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault())
    return format.format(dateInput)
}
