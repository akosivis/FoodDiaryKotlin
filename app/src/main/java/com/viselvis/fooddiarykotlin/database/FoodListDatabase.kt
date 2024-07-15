package com.viselvis.fooddiarykotlin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.viselvis.fooddiarykotlin.database.converters.ListConverter
import com.viselvis.fooddiarykotlin.database.converters.TimestampConverter

@Database(entities = [FoodItemModel::class], version = 4, exportSchema = false )
@TypeConverters(ListConverter::class, TimestampConverter::class)
abstract class FoodListDatabase: RoomDatabase() {

    abstract fun foodItemDao(): FoodItemDao

    companion object {
        private var INSTANCE: FoodListDatabase? = null

        fun getInstance(context: Context): FoodListDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    val arrayListConverter = ListConverter()
                    val timeStampConverter = TimestampConverter()
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FoodListDatabase::class.java,
                            "food_database")
                            .fallbackToDestructiveMigration()
                            .addTypeConverter(arrayListConverter)
                            .addTypeConverter(timeStampConverter)
                            .build()
                }

                return instance
            }
        }
    }
}