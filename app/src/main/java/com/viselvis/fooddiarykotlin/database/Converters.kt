package com.viselvis.fooddiarykotlin.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class Converters {
    @TypeConverter
    fun arrayListToString(list: ArrayList<String>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToArrayList(string: String?): ArrayList<String>? {
        val listType = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(string, listType)
    }

    @TypeConverter
    fun timeStampToDate(value: Long?): Date? {
        return if (value == null) {
            null
        } else {
            Date(value)
        }
    }

    @TypeConverter
    fun dateToTimeStamp(date: Date?): Long? = date?.time
}