package com.viselvis.fooddiarykotlin.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object NoteEatDestinations {
    const val HOME_ROUTE = "home"
    const val PRINT_ROUTE = "print"
    const val SETTINGS_ROUTE = "settings"
    const val ADD_FOOD_ITEM_ROUTE = "add_food"
    const val FOOD_HISTORY_ROUTE = "view_food_history"
    const val SELECT_FOOD_TYPE_ROUTE = "select_food_type"
}

class NoteEatNavigationActions(navController: NavHostController) {
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
    val navigateToAddFoodItem: () -> Unit = {
        navController.navigate(NoteEatDestinations.ADD_FOOD_ITEM_ROUTE) {

        }
    }
    val navigateToViewFoodHistory: () -> Unit = {
        navController.navigate(NoteEatDestinations.FOOD_HISTORY_ROUTE) {

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
}