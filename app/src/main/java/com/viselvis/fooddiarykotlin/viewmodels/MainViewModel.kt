package com.viselvis.fooddiarykotlin.viewmodels

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.*
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(private val repo: FoodItemRepository): ViewModel() {
    // val allFoodItems: LiveData<List<FoodItemModel>> = repo.firstThreeFoodItems.asLiveData()
    private val viewModelState = MutableStateFlow(MainViewModelState(latestFoodItems = emptyList()))
    val uiState = viewModelState

    init {
        viewModelScope.launch {
            repo.firstThreeFoodItems.collect { items ->
                viewModelState.update { it.copy(latestFoodItems = items) }
            }
        }
    }
}

data class MainViewModelState(
    val latestFoodItems: List<FoodItemModel>? = null
)

class MainViewModelFactory(private val repo: FoodItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}