package com.viselvis.fooddiarykotlin.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.viselvis.fooddiarykotlin.application.FoodItemListApplication
import com.viselvis.fooddiarykotlin.ui.routes.HomeRoute
import com.viselvis.fooddiarykotlin.ui.routes.PrintListRoute
import com.viselvis.fooddiarykotlin.ui.routes.SelectFoodTypeRoute
import com.viselvis.fooddiarykotlin.ui.routes.SettingsRoute
import com.viselvis.fooddiarykotlin.viewmodels.MainViewModel
import com.viselvis.fooddiarykotlin.viewmodels.MainViewModelFactory
import com.viselvis.fooddiarykotlin.viewmodels.PrintFoodDiaryViewModel
import com.viselvis.fooddiarykotlin.viewmodels.PrintFoodDiaryViewModelFactory

@Composable
fun NoteEatNavGraph(
    modifier: Modifier,
    application: FoodItemListApplication,
    openDrawer: () -> Unit = {},
    navController: NavHostController = rememberNavController(),
    startNavigation: String = NoteEatDestinations.HOME_ROUTE,
    navigateToSelectFoodTypeRoute: () -> Unit,
    navigateToAddFoodRoute: () -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startNavigation
    ) {
        composable(NoteEatDestinations.HOME_ROUTE) {
            val viewModel: MainViewModel = viewModel(
                factory = MainViewModelFactory(application.repository)
            )
            HomeRoute(
                viewModel = viewModel,
                navigateToSelectFoodType = navigateToSelectFoodTypeRoute
            )
        }

        composable(NoteEatDestinations.PRINT_ROUTE) {
            val viewModel: PrintFoodDiaryViewModel = viewModel(
                factory = PrintFoodDiaryViewModelFactory(application.repository)
            )
            PrintListRoute(printFoodDiaryViewModel = viewModel)
        }

        composable(NoteEatDestinations.SETTINGS_ROUTE) {
            SettingsRoute()
        }

//        composable(NoteEatDestinations.ADD_FOOD_ITEM_ROUTE) {
//
//        }

        composable(NoteEatDestinations.FOOD_HISTORY_ROUTE) {
            SettingsRoute()
        }

        composable(NoteEatDestinations.SELECT_FOOD_TYPE_ROUTE) {
            SelectFoodTypeRoute(navigateToAddFoodItem = navigateToAddFoodRoute())
        }
    }
}