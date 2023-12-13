package com.viselvis.fooddiarykotlin.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object NoteEatDestinations {
    const val HOME_ROUTE = "home"
    const val PRINT_ROUTE = "print"
    const val SETTINGS_ROUTE = "settings"
    const val ADD_FOOD_ITEM_ROUTE = "add_food"
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
}