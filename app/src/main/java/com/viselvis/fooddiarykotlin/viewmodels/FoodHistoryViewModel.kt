package com.viselvis.fooddiarykotlin.viewmodels

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
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
    var currentFoodItemListState by mutableStateOf(
        FoodItemListState(
            givenDate = Calendar.getInstance(),
            foodItems = ,
        )
    )

    fun getFoodItemsByDate() {

    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDay(): CharSequence? {
        val sdf = SimpleDateFormat("EEEE")
        var date = daySelected.time
        return sdf.format(date)
    }

    private fun getCurrentDate(): CharSequence? {

    }
}

data class FoodItemListState (
    var givenDate: Calendar? = null,
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