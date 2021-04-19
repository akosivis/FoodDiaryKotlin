package com.viselvis.fooddiarykotlin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FoodItemModel::class], version = 1, exportSchema = false )
abstract class FoodListDatabase: RoomDatabase() {

    abstract fun foodItemDao(): FoodItemDao

    companion object {
        private var INSTANCE: FoodListDatabase? = null

        fun getInstance(context: Context): FoodListDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FoodListDatabase::class.java,
                            "food_database")
                            .fallbackToDestructiveMigration()
                            .build()
                }

                return instance
            }
        }
    }
}