package com.viselvis.fooddiarykotlin.database

import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.viselvis.fooddiarykotlin.MainActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.*

class FoodItemRepository(
    private val foodItemDao: FoodItemDao
) {
    val allFoodItems: Flow<List<FoodItemModel>> = foodItemDao.getAllFoodItems()
    val firstThreeFoodItems: Flow<List<FoodItemModel>> = foodItemDao.getFirstThreeFoodItems()

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

    fun getFoodItemOnGivenId(foodItemId: Long): FoodItemModel? {
        return foodItemDao.getFoodItemById(foodItemId)
    }
}