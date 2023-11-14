package com.viselvis.fooddiarykotlin.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import java.util.*

class FoodItemRepository(private val foodItemDao: FoodItemDao ) {
    val allFoodItems: Flow<List<FoodItemModel>> = foodItemDao.getAllFoodItems()
    val firstThreeFoodItems: Flow<List<FoodItemModel>> = foodItemDao.getFirstThreeFoodItems()
    // val foodItemsByRange: Flow<List<FoodItemModel>> = foodItemDao.getFoodItemsByDateRange(startDate, endDate)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertFoodItem(foodItem: FoodItemModel): Long {
        return foodItemDao.insertFoodItem(foodItem)
    }

    // val foodItemsByRange: Flow<List<FoodItemModel>> get() =

    fun getFoodItemsByRange(startDate: Long, endDate: Long): Flow<List<FoodItemModel>> {
        return foodItemDao.getFoodItemsByDateRange(startDate, endDate)
    }

    fun getFoodItemsOnGivenDate(startDay: Long, endDay: Long): List<FoodItemModel> {
        return foodItemDao.getFoodItemsByDate(startDay, endDay)
    }
}