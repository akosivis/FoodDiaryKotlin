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
    private val dataStore: DataStore<Preferences>,
    private val foodItemDao: FoodItemDao
) {
    object PreferencesKeys {
        val USERNAME = stringPreferencesKey("username")
    }

    val allFoodItems: Flow<List<FoodItemModel>> = foodItemDao.getAllFoodItems()
    val firstThreeFoodItems: Flow<List<FoodItemModel>> = foodItemDao.getFirstThreeFoodItems()
    // val foodItemsByRange: Flow<List<FoodItemModel>> = foodItemDao.getFoodItemsByDateRange(startDate, endDate)
    val usernameFlow: Flow<String> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[PreferencesKeys.USERNAME] ?: ""
        }

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

    suspend fun writeUserName(input: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USERNAME] = input
        }
    }
}