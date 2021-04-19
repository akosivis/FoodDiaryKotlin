package com.viselvis.fooddiarykotlin.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class FoodItemRepository(private val foodItemDao: FoodItemDao ) {
    val allFoodItems: Flow<List<FoodItemModel>> = foodItemDao.getAllFoodItems()
    val firstThreeFoodItems: Flow<List<FoodItemModel>> = foodItemDao.getFirstThreeFoodItems()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertFoodItem(foodItem: FoodItemModel): Long {
        return foodItemDao.insertFoodItem(foodItem)
    }
}