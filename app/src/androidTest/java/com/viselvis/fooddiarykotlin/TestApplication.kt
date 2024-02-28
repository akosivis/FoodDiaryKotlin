package com.viselvis.fooddiarykotlin

import android.app.Application
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import com.viselvis.fooddiarykotlin.database.FoodListDatabase

class TestApplication: Application() {
    val database by lazy { FoodListDatabase.getInstance(this) }
    // val repository by lazy { FoodItemRepository(database.foodItemDao()) }
}