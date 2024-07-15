package com.viselvis.fooddiarykotlin.database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.Date

@ProvidedTypeConverter
class TimestampConverter {

    @TypeConverter
    fun timeStampToDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimeStamp(date: Date): Long = date.time
}