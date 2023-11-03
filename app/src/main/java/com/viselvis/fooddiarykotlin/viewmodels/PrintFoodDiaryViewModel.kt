package com.viselvis.fooddiarykotlin.viewmodels

import androidx.lifecycle.*
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.util.*

class PrintFoodDiaryViewModel(private val repo: FoodItemRepository): ViewModel() {
//    private var _foodItemsByRange = MutableLiveData<List<FoodItemModel>>()
//    val foodItemsByRange: LiveData<List<FoodItemModel>>
//        get() = _foodItemsByRange

    // var foodItemsByRange: LiveData<List<FoodItemModel>> =
    // val foodItemsByRange : StateFlow<List<FoodItemModel>> = _foodItemsByRange

    // val isDataInserted: LiveData<List<FoodItemModel>> = TODO()
//    val isDataInserted: LiveData<Long>
//        get() = _isDataInserted

    // val foodItemsByRange: LiveData<List<FoodItemModel>> = repo.foodItemsByRange?.asLiveData() ?:

//    fun getFoodItemsByRange(fromDate: Date, toDate: Date) {
//        viewModelScope.launch(Dispatchers.IO) {
//             foodItemsByRange  = repo.getFoodItemsByRange(fromDate, toDate)
//                .flatMapConcat { it.asFlow() }
//                .toList()
//        }
//    }

    fun getFoodItemsByRange(fromDate: Long, toDate: Long): Flow<List<FoodItemModel>> = repo.getFoodItemsByRange(fromDate, toDate)

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