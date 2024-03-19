package com.viselvis.fooddiarykotlin.viewmodels

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.*
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import com.viselvis.fooddiarykotlin.database.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

data class MainViewModelState(
    val isLoading: Boolean = false,
    val hasFinishedWalkthrough: Boolean = false,
    val walkThroughPage: Int = 0,
    val userNameState: EnterUsernameState = EnterUsernameState(
        userName = "",
        isThereUserName = true
    )
)

class MainViewModel(
    private val userRepo: UserRepository): ViewModel() {

    private val viewModelState = MutableStateFlow(
        MainViewModelState(
            isLoading = true,
        )
    )
    val uiState = viewModelState

    init {
        viewModelScope.launch {
            fetchUserName()
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

data class EnterUsernameState (
    var userName: String,
    var isThereUserName: Boolean,
)

class MainViewModelFactory(private val userRepo: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(userRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}