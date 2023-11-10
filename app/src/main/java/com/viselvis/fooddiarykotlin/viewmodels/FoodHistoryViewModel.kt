package com.viselvis.fooddiarykotlin.viewmodels

import android.annotation.SuppressLint
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.viselvis.fooddiarykotlin.adapter.FoodItemAdapter
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class FoodHistoryViewModel(private val repo: FoodItemRepository): ViewModel() {
    val allFoodItems: LiveData<List<FoodItemModel>> = repo.allFoodItems.asLiveData()
    // val foodItemsByDate: LiveData<List<FoodItemModel>> = repo.byDateFoodItems.asLive
    val foodItemsForGivenDate by mutableStateOf(listOf<FoodItemModel>())
    var daySelected by mutableStateOf(Calendar.getInstance())
    var dateToday by mutableStateOf(Calendar.getInstance())
    var dateToDisplay by mutableStateOf("")
    var dayToDisplay by mutableStateOf("")
    var currentFoodItemListState by mutableStateOf(
        FoodItemListState(
            givenCalendarInstance = Calendar.getInstance(),
            foodItems = emptyList(),
        )
    )

    init {
        currentFoodItemListState.let {
            it.givenCalendarInstance = Calendar.getInstance()
            it.foodItems = foodItemsListByDate(it.givenCalendarInstance)
        }
        getDayDetails()
    }

    private fun foodItemsListByDate(date: Calendar?) : List<FoodItemModel> {
        val toReturnList = mutableListOf<FoodItemModel>()

        for (model in allFoodItems.value!!) {
            val dateObject = model.foodItemCreated
            val cal: Calendar = Calendar.getInstance()
            cal.time = dateObject

            if (date != null) {
                if (compareDate(cal, date)) { // if foodItemCreated is date today
                    toReturnList.add(model)
                }
            }
        }
        // showOrHide(toReturnList.size)
        return toReturnList
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
        daySelected.add(Calendar.DAY_OF_YEAR, -1)
        getDayDetails()
        // binding.rltNextDate.isEnabled = true
        // binding.rltNextDate.visibility = View.VISIBLE
    }

    fun getNextDay() {
        daySelected.add(Calendar.DAY_OF_YEAR, 1)
        getDayDetails()
    }

    private fun getDayDetails() {
        dateToDisplay = getCurrentDay() as String
        dayToDisplay = getCurrentDate() as String
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDay(): CharSequence? {
        val sdf = SimpleDateFormat("EEEE")
        val date = currentFoodItemListState.givenCalendarInstance?.time
        return date.let { sdf.format(it) }
    }

    private fun getCurrentDate(): CharSequence? {
        var date = daySelected.time
        // currentFoodItemListState.givenCalendarInstance = daySelected.time
        return DateFormat.getDateInstance().format(date)
    }
}

data class FoodItemListState (
    var givenCalendarInstance: Calendar? = null,
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