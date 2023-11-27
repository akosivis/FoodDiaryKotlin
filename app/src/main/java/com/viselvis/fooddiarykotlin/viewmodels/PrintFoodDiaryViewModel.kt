package com.viselvis.fooddiarykotlin.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

class PrintFoodDiaryViewModel(private val repo: FoodItemRepository): ViewModel() {

    var showDatePicker by mutableStateOf(false)
    var dateToday: Calendar = Calendar.getInstance()
    var fromDateSelected by mutableStateOf(0L)
    var toDateSelected by mutableStateOf(0L)
    var dateSelected by mutableStateOf(dateToday.timeInMillis)

    // 0 - from Date display is clicked,
    // 1 - to Date display is clicked
    var dateDisplayClicked by mutableStateOf(-1)
    var fromDateStringDisplay by mutableStateOf(
        longToStringDisplay(fromDateSelected)
    )
    var toDateStringDisplay by mutableStateOf(
        longToStringDisplay(toDateSelected)
    )
    var allowGeneratePDF by mutableStateOf(true)

    private fun setSelectedDates(fromDate: Long, toDate: Long) {

        // val calendar = Calendar.getInstance()
        // calendar.time = Date() // Set your date object here

//        calendar.set(Calendar.HOUR_OF_DAY, 23)
//        calendar.set(Calendar.MINUTE, 59)
//        calendar.set(Calendar.SECOND, 59)
//        calendar.time // Your changed date o

        val calendar = Calendar.getInstance()
        calendar.time = toDate?.let { Date(it) }
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.time

//        val toDateEditHours = toDate?.let { Date(it) }
//        toDateEditHours.let {
//            it
//        }set(Calendar.HOUR_OF_DAY, 23)
//        toDateEditHours.set(Calendar.MINUTE, 59)
//        toDateEditHours.set(Calendar.SECOND, 59)
//        calendar.time


        // fromDateSelected = fromDate
        // toDateSelected = calendar.timeInMillis
        // toDateSelected = toDate?.let { Date(it) }

        fromDateStringDisplay = longToStringDisplay(fromDate) as String
        toDateStringDisplay = longToStringDisplay(toDate) as String

//        this.lifecycleScope.launch {
//            viewMo
//        }
    }

    fun longToStringDisplay(date: Long): CharSequence {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date

        val mMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
        val mDay = calendar[Calendar.DAY_OF_MONTH]
        val mYear = calendar[Calendar.YEAR]

        return "$mDay $mMonth $mYear"
    }

    private fun showDateRangePicker() {
        // picker.show(activity?.supportFragmentManager!!, picker.toString())
    }

    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return df.parse(date).time
    }

    private fun getAllFoodItemsFromGivenRange() {
        if (fromDateSelected != null && toDateSelected != null) {
            viewModelScope.launch {
//                printFoodDiaryViewModel.getFoodItemsByRange(fromDateSelected!!, toDateSelected!!).collect {
//                    foodItemListByRange = it
//
//                    for (i in foodItemListByRange) {
//                        Log.d(TAG, "Food item: ${i.foodItemTitle}")
//                    }
//                    // Toast.makeText(activity, "Size is ${foodItemListByRange.size}", Toast.LENGTH_SHORT).show()
//                }
            }
        } else {
            // Toast.makeText(context, "Problem!", Toast.LENGTH_SHORT).show()
        }
    }

    fun getFoodItemsByRange(fromDate: Long, toDate: Long): Flow<List<FoodItemModel>> = repo.getFoodItemsByRange(fromDate, toDate)

    fun getFoodItems(fromDate: Long, toDate: Long) {
        Log.d(TAG, "getFoodItems: $fromDate and $toDate")
    }

    companion object {
        private const val TAG = "PrintFoodDiaryViewModel"
    }
}

class PrintFoodDiaryViewModelFactory(private val repo: FoodItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrintFoodDiaryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PrintFoodDiaryViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}