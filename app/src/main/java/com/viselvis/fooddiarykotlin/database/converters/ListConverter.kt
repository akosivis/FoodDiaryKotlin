package com.viselvis.fooddiarykotlin.database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.ArrayList

@ProvidedTypeConverter
class ListConverter {
    @TypeConverter
    fun listToString(list: List<String>): String {
        if (list.isEmpty()) {
            return ""
        } else {
            var itemsInStringForm = ""
            for (item in list) {
                itemsInStringForm += "$item,"
            }
            return itemsInStringForm
        }
    }

    @TypeConverter
    fun stringToList(string: String): List<String> {
        val listOfItems = mutableListOf<String>()
        val items = string.split(",".toRegex()).dropLastWhile {
            it.isEmpty()
        }

        for (s in items) {
            if (s.isNotEmpty()) {
                listOfItems.add(s)
            }
        }
        return listOfItems
    }
}