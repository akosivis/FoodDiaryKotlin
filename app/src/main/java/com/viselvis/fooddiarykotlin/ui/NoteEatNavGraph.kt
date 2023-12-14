package com.viselvis.fooddiarykotlin.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.viselvis.fooddiarykotlin.application.FoodItemListApplication
import com.viselvis.fooddiarykotlin.fragments.MainFragment
import com.viselvis.fooddiarykotlin.ui.routes.HomeRoute
import com.viselvis.fooddiarykotlin.ui.routes.PrintListRoute
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
    startNavigation: String = NoteEatDestinations.HOME_ROUTE
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
            HomeRoute(viewModel = viewModel)
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
    }
}