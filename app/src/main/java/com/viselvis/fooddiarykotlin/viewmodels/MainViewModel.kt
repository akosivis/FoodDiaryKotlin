package com.viselvis.fooddiarykotlin.viewmodels

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.*
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import com.viselvis.fooddiarykotlin.database.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

sealed interface HomeRouteState {
    val isLoading: Boolean
    val userNameState: EnterUsernameState

    data class Walkthrough(
        val walkthroughPage: Int,
        override val isLoading: Boolean,
        override val userNameState: EnterUsernameState
    ) : HomeRouteState

    data class MainContent(
        val latestFoodItems: List<FoodItemModel>,
        override val isLoading: Boolean,
        override val userNameState: EnterUsernameState
    ): HomeRouteState
}

private data class HomeViewModelState(
    val isLoading: Boolean = false,
    val hasFinishedWalkthrough: Boolean = false,
    val walkThroughPage: Int = 0,
    val userNameState: EnterUsernameState = EnterUsernameState(
        userName = "",
        isThereUserName = true
    ),
    val latestFoodItems: List<FoodItemModel> = emptyList()
) {
    fun toUiState(): HomeRouteState =
        if (hasFinishedWalkthrough) {
            HomeRouteState.MainContent (
                isLoading = isLoading,
                latestFoodItems = latestFoodItems,
                userNameState = userNameState,
            )
        } else {
            HomeRouteState.Walkthrough(
                isLoading = isLoading,
                walkthroughPage = walkThroughPage,
                userNameState = userNameState,
            )
        }
}

class MainViewModel(
    private val foodItemsRepo: FoodItemRepository,
    private val userRepo: UserRepository): ViewModel() {

    private val viewModelState = MutableStateFlow(
        HomeViewModelState(
            isLoading = true,
        )
    )
    val uiState = viewModelState
        .map(HomeViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        viewModelScope.launch {
            fetchUserName()
            fetchFoodItems()
        }
    }

    private fun fetchUserName() {
        viewModelScope.launch {
            userRepo.userFlow.collect { user ->
                viewModelState.update {
                    it.copy (
                        userNameState = EnterUsernameState(
                            userName = user.userName,
                            isThereUserName = user.userName != ""
                        ),
                        hasFinishedWalkthrough = user.hasFinishedWalkthrough
                    )
                }
            }
        }
    }

    private fun fetchFoodItems() {
        viewModelScope.launch {
            foodItemsRepo.firstThreeFoodItems.collect { items ->
                viewModelState.update {
                    it.copy(
                        latestFoodItems = items
                    )
                }
            }
        }
    }

    fun switchPage(isNext: Boolean) {
        viewModelState.update {
            it.copy (
                walkThroughPage = if (isNext) {
                    it.walkThroughPage + 1
                } else {
                    it.walkThroughPage - 1
                } ,
                hasFinishedWalkthrough = (it.walkThroughPage > 3)
            )
        }

        if (viewModelState.value.walkThroughPage > 3) {
            viewModelScope.launch {
                userRepo.finishWalkthrough()
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