package com.viselvis.fooddiarykotlin.viewmodels

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.*
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(private val repo: FoodItemRepository): ViewModel() {
    private val viewModelState = MutableStateFlow(MainViewModelState(latestFoodItems = emptyList()))
    val uiState = viewModelState
    private var _userNameState = MutableStateFlow(
        EnterUsernameState(
            userName = "",
            isThereUserName = true
        )
    )
    val userNameState: StateFlow<EnterUsernameState> = _userNameState.asStateFlow()

    init {
        viewModelScope.launch {
            repo.usernameFlow.collect { name ->
                _userNameState.update {
                    it.copy(
                        userName = name,
                        isThereUserName = name != ""
                    )
                }
            }

            repo.firstThreeFoodItems.collect { items ->
                viewModelState.update { it.copy(latestFoodItems = items) }
            }
        }
    }
}

data class MainViewModelState(
    val latestFoodItems: List<FoodItemModel>? = null
)

data class EnterUsernameState (
    var userName: String,
    var isThereUserName: Boolean,
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