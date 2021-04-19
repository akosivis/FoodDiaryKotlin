package com.viselvis.fooddiarykotlin.application

import android.app.Application
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import com.viselvis.fooddiarykotlin.database.FoodListDatabase

class FoodItemListApplication: Application() {
    val database by lazy { FoodListDatabase.getInstance(this) }
    val repository by lazy { FoodItemRepository(database.foodItemDao()) }
}