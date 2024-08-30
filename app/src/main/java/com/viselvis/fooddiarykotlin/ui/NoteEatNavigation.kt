package com.viselvis.fooddiarykotlin.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object NoteEatDestinations {
    const val ENTER_NAME_ROUTE = "enter_name"
    const val INTRODUCTION_ROUTE = "intro"
    const val MAIN_ROUTE = "main"
    const val HOME_ROUTE = "home"
    const val PRINT_ROUTE = "print"
    const val SETTINGS_ROUTE = "settings"
    const val ADD_FOOD_ITEM_ROUTE = "add_edit_food"
    const val FOOD_HISTORY_ROUTE = "view_food_history"
    const val SELECT_FOOD_TYPE_ROUTE = "select_food_type"
    const val ITEM_DETAIL_ROUTE = "item_detail"
}

class NoteEatNavigationActions(navController: NavHostController) {
    val navigateToMainRoute: () -> Unit = {
        navController.navigate(NoteEatDestinations.MAIN_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToHome: () -> Unit = {
        navController.navigate(NoteEatDestinations.HOME_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToPrintList: () -> Unit = {
        navController.navigate(NoteEatDestinations.PRINT_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToSettings: () -> Unit = {
        navController.navigate(NoteEatDestinations.SETTINGS_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToSelectFoodType: () -> Unit = {
        navController.navigate(NoteEatDestinations.SELECT_FOOD_TYPE_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToAddEditFoodItem: (Int) -> Unit = { foodType ->
        navController.navigate(
            "${NoteEatDestinations.ADD_FOOD_ITEM_ROUTE}/${foodType}") {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            // restoreState = true
        }
    }
    val navigateToViewFoodHistory: () -> Unit = {
        navController.navigate(NoteEatDestinations.FOOD_HISTORY_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToEnterUserName: () -> Unit = {
        navController.navigate(NoteEatDestinations.ENTER_NAME_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToIntroPage: () -> Unit = {
        navController.navigate(NoteEatDestinations.INTRODUCTION_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val navigateToItemDetailPage: (Long) -> Unit = { foodItemId ->
        navController.navigate("${NoteEatDestinations.ITEM_DETAIL_ROUTE}/${foodItemId}") {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            // restoreState = true
        }
    }
}