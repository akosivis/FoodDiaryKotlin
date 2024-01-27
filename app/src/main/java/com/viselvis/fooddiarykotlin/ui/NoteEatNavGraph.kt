package com.viselvis.fooddiarykotlin.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.viselvis.fooddiarykotlin.application.FoodItemListApplication
import com.viselvis.fooddiarykotlin.ui.routes.*
import com.viselvis.fooddiarykotlin.viewmodels.*

@Composable
fun NoteEatNavGraph(
    modifier: Modifier,
    application: FoodItemListApplication,
    openDrawer: () -> Unit = {},
    navController: NavHostController = rememberNavController(),
    startNavigation: String = NoteEatDestinations.ENTER_NAME_ROUTE,
    navigateToSelectFoodTypeRoute: () -> Unit,
    navigateToAddFoodRoute: (Int) -> Unit,
    navigateToFoodHistory: () -> Unit,
    navigateToMainRoute: () -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startNavigation
    ) {
        composable(NoteEatDestinations.ENTER_NAME_ROUTE) {
            val viewModel: EnterUsernameViewModel = viewModel(
                factory = EnterUsernameViewModelFactory(application.repository)
            )
            EnterNameRoute(
                viewModel,
                navigateToMainRoute
            )
        }

        composable(NoteEatDestinations.INTRODUCTION_ROUTE) {
            IntroductionRoute(navigateToMainRoute)
        }

        navigation(
            startDestination = NoteEatDestinations.HOME_ROUTE,
            route = NoteEatDestinations.MAIN_ROUTE
        ) {
            composable(NoteEatDestinations.HOME_ROUTE) {
                val viewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(application.repository)
                )
                HomeRoute(
                    viewModel = viewModel,
                    navigateToSelectFoodType = navigateToSelectFoodTypeRoute,
                    navigateToFoodHistory = navigateToFoodHistory
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

            composable(NoteEatDestinations.SELECT_FOOD_TYPE_ROUTE) {
                SelectFoodTypeRoute(navigateToAddFoodItem = navigateToAddFoodRoute)
            }

            composable(
                "${NoteEatDestinations.ADD_FOOD_ITEM_ROUTE}/{type}",
                arguments = listOf(navArgument("type") { type = NavType.IntType } )
            ) { backStackEntry ->
                val viewModel: AddFoodItemViewModel = viewModel(
                    factory = AddFoodItemViewModelFactory(application.repository)
                )
                AddFoodItemRoute(
                    navController = navController,
                    viewModel = viewModel,
                    backStackEntry.arguments?.getInt("type")
                )
            }

            composable(NoteEatDestinations.FOOD_HISTORY_ROUTE) {
                val viewModel: FoodHistoryViewModel = viewModel(
                    factory = FoodHistoryViewModelFactory(application.repository)
                )
                FoodHistoryPage(viewModel = viewModel)
            }

        }
    }
}