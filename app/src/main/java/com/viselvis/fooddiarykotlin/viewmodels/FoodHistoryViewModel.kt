package com.viselvis.fooddiarykotlin.viewmodels

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class FoodHistoryViewModel(private val repo: FoodItemRepository): ViewModel() {
    val allFoodItems: LiveData<List<FoodItemModel>> = repo.allFoodItems.asLiveData()
    // val foodItemsByDate: LiveData<List<FoodItemModel>> = repo.byDateFoodItems.asLive
    var foodItemsForGivenDate by mutableStateOf(listOf<FoodItemModel>())
    var daySelected by mutableStateOf(Calendar.getInstance())
    var dateToday by mutableStateOf(Calendar.getInstance())
    var dateToDisplay by mutableStateOf("")
    var dayToDisplay by mutableStateOf("")
    private val currentFoodItemListState = MutableStateFlow(
        FoodItemListState(
            givenCalendarInstance = Calendar.getInstance(),
            foodItems = emptyList(),
        )
    )
    val uiState = currentFoodItemListState
//    var currentFoodItemListState by mutableStateOf(
//        FoodItemListState(
//            givenCalendarInstance = Calendar.getInstance(),
//            foodItems = emptyList(),
//        )
//    )

    init {
        getDayDetails()
        foodItemsListByDate(currentFoodItemListState.value.givenCalendarInstance)
    }

    private fun foodItemsListByDate(dateInstance: Calendar) {
        val dateStart = dateInstance.clone() as Calendar
        dateStart.let {
            it.set(Calendar.HOUR_OF_DAY, 0)
            it.set(Calendar.MINUTE, 0)
            it.set(Calendar.SECOND, 0)
        }

        val dateEnd = dateInstance.clone() as Calendar
        dateEnd.let {
            it.set(Calendar.HOUR_OF_DAY, 23)
            it.set(Calendar.MINUTE, 59)
            it.set(Calendar.SECOND, 59)
        }

        viewModelScope.launch(Dispatchers.IO) {
            repo.getFoodItemsOnGivenDate(dateStart.timeInMillis, dateEnd.timeInMillis).apply {
                try {
                    currentFoodItemListState.update {
                        it.copy(foodItems = this)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        currentFoodItemListState.update {
                            it.copy(foodItems = emptyList())
                        }
                    }
                }
            }
        }
    }

    private fun compareDate(dateObject: Calendar, date: Calendar): Boolean {
        val dateFoodItemDay = dateObject.get(Calendar.DAY_OF_MONTH)
        val dateFoodItemMonth = dateObject.get(Calendar.MONTH)
        val dateFoodItemYear = dateObject.get(Calendar.YEAR)
        val dateSelectedDay = date.get(Calendar.DAY_OF_MONTH)
        val dateSelectedMonth = date.get(Calendar.MONTH)
        val dateSelectedYear = date.get(Calendar.YEAR)

        if((dateFoodItemDay == dateSelectedDay) &&
            (dateFoodItemMonth == dateSelectedMonth) && (dateFoodItemYear == dateSelectedYear) ) {
            return true
        }
        return false;
    }

    fun getPreviousDay() {
        currentFoodItemListState.value.givenCalendarInstance.add(Calendar.DAY_OF_YEAR, -1)
        getDayDetails()
        foodItemsListByDate(currentFoodItemListState.value.givenCalendarInstance)
    }

    fun getNextDay() {
        currentFoodItemListState.value.givenCalendarInstance.add(Calendar.DAY_OF_YEAR, 1)
        getDayDetails()
        foodItemsListByDate(currentFoodItemListState.value.givenCalendarInstance)
    }

    private fun getDayDetails() {
        dateToDisplay = getCurrentDay() as String
        dayToDisplay = getCurrentDate() as String
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDay(): CharSequence? {
        val sdf = SimpleDateFormat("EEEE")
        val date = currentFoodItemListState.value.givenCalendarInstance.time
        return date.let { sdf.format(it) }
    }

    private fun getCurrentDate(): CharSequence? {
        var date = currentFoodItemListState.value.givenCalendarInstance.time
        return DateFormat.getDateInstance().format(date)
    }

    fun getTimeFromLong(foodItemCreated: Long): CharSequence? {
        val date = Date(foodItemCreated)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }
}

data class FoodItemListState (
    var givenCalendarInstance: Calendar,
    var foodItems: List<FoodItemModel>,

//    fun getGivenDate(): CharSequence? {
//        date = daySelected.time
//        dateSelected = daySelected.time
//        return DateFormat.getDateInstance().format(date)
//    }
)

class FoodHistoryViewModelFactory(private val repo: FoodItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FoodHistoryViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}