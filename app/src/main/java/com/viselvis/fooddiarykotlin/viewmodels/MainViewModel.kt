package com.viselvis.fooddiarykotlin.viewmodels

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.*
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import com.viselvis.fooddiarykotlin.database.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

sealed interface HomeRouteState {
    val isLoading: Boolean
    val isFirstTimeLogin: Boolean

    data class FirstTimeLogin(
        val introPage: Int,
        override val isLoading: Boolean,
        override val isFirstTimeLogin: Boolean
    ) : HomeRouteState

    data class NotFirstTimeLogin(
        val userNameState: EnterUsernameState,
        val latestFoodItems: List<FoodItemModel>,
        override val isLoading: Boolean,
        override val isFirstTimeLogin: Boolean
    ): HomeRouteState
}

class MainViewModel(
    private val foodItemsRepo: FoodItemRepository,
    private val userRepo: UserRepository): ViewModel() {

    private var _userNameState = MutableStateFlow(
        EnterUsernameState(
            userName = "",
            isThereUserName = true
        )
    )
    val userNameState: StateFlow<EnterUsernameState> = _userNameState.asStateFlow()
    val latestFoodItems: LiveData<List<FoodItemModel>> = foodItemsRepo.firstThreeFoodItems.asLiveData()

    init {
        viewModelScope.launch {
            userRepo.userFlow.collect { user ->
                _userNameState.update {
                    it.copy(
                        userName = user.userName,
                        isThereUserName = user.userName != ""
                    )
                }
            }

//            repo.firstThreeFoodItems.collect { items ->
//                viewModelState.update {
//                    it.copy(latestFoodItems = items)
//                }
//            }
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

class MainViewModelFactory(
    private val foodItemsRepo: FoodItemRepository, private val userRepo: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(foodItemsRepo, userRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}