package com.viselvis.fooddiarykotlin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.viselvis.fooddiarykotlin.application.FoodItemListApplication
import com.viselvis.fooddiarykotlin.ui.theme.NoteEatTheme
import com.viselvis.fooddiarykotlin.viewmodels.HomeRouteState
import com.viselvis.fooddiarykotlin.viewmodels.MainViewModel
import com.viselvis.fooddiarykotlin.viewmodels.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NoteEatApp(
    app: FoodItemListApplication
) {
    NoteEatTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            NoteEatNavigationActions(navController)
        }
        val viewModel: MainViewModel = viewModel(
            factory = MainViewModelFactory(
                app.userRepo
            )
        )
        val coroutineScope = rememberCoroutineScope()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: NoteEatDestinations.INTRODUCTION_ROUTE
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val closeDrawer: () -> Unit = { coroutineScope.launch { drawerState.close() } }

        NoteEatAppScreen(
            app = app,
            navController = navController,
            coroutineScope = coroutineScope,
            drawerState = drawerState,
            currentRoute = currentRoute,
            closeDrawer = closeDrawer,
            navigationActions = navigationActions,
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEatAppScreen(
    app: FoodItemListApplication,
    navController: NavHostController,
    drawerState: DrawerState,
    currentRoute: String,
    closeDrawer: () -> Unit,
    navigationActions: NoteEatNavigationActions,
    coroutineScope: CoroutineScope,
    viewModel: MainViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (!uiState.userNameState.isThereUserName) {
        navigationActions.navigateToEnterUserName
    }

    Surface (
        modifier = Modifier.fillMaxSize()
    ) {
        Box {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .padding(15.dp)) {
                            Text("NoteEat")
                            Divider()
                            NavigationDrawerItem(
                                label = { Text("Home") },
                                selected = currentRoute == NoteEatDestinations.HOME_ROUTE,
                                onClick = {
                                    navigationActions.navigateToHome()
                                    closeDrawer()
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("Print") },
                                selected = currentRoute == NoteEatDestinations.PRINT_ROUTE,
                                onClick = {
                                    navigationActions.navigateToPrintList()
                                    closeDrawer()
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("Settings") },
                                selected = currentRoute == NoteEatDestinations.SETTINGS_ROUTE,
                                onClick = {
                                    navigationActions.navigateToSettings()
                                    closeDrawer()
                                }
                            )
                        }
                    }
                },
                gesturesEnabled = true
            ) {
                Scaffold(
                    topBar = {
                        if (
                            currentRoute != NoteEatDestinations.INTRODUCTION_ROUTE &&
                            currentRoute != NoteEatDestinations.ENTER_NAME_ROUTE
                        ) {
                            TopAppBar(
                                title = { },
                                navigationIcon = {
                                    Icon(
                                        modifier = Modifier.clickable(
                                            onClick = {
                                                coroutineScope.launch {
                                                    if (drawerState.isClosed) {
                                                        drawerState.open()
                                                    } else {
                                                        drawerState.close()
                                                    }
                                                }
                                            }
                                        ),
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Menu"
                                    )
                                }
                            )
                        }
                    },
                ) { innerPadding ->
                    NoteEatNavGraph(
                        modifier = Modifier.padding(innerPadding),
                        application = app,
                        navController = navController,
                        openDrawer = { coroutineScope.launch { drawerState.open() } },
                        navigateToSelectFoodTypeRoute = navigationActions.navigateToSelectFoodType,
                        navigateToAddFoodRoute = navigationActions.navigateToAddEditFoodItem,
                        navigateToFoodHistory = navigationActions.navigateToViewFoodHistory,
                        navigateToMainRoute = navigationActions.navigateToMainRoute,
                        navigateToEnterUsername = navigationActions.navigateToEnterUserName,
                        navigateToIntroPage = navigationActions.navigateToIntroPage
                    )
                }
            }

            if (!uiState.hasFinishedWalkthrough) {
                Box (
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Black.copy(alpha = 0.5f))
                        .padding(15.dp)
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text (
                            text = getWalkthroughText(uiState.walkThroughPage),
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer (modifier = Modifier.height(15.dp))
                        Text (
                            modifier = Modifier.clickable { viewModel.switchPage(true) },
                            text = if (uiState.walkThroughPage < 3)
                                "Next" else "Done",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                }
            }
        }
    }
}

fun getWalkthroughText(page: Int): String = when (page) {
    0 -> "Welcome to NoteEat app!"
    1 -> "You can list your recent food intakes here."
    2 -> "You can also print your food intake on a given date."
    else -> "Hope you can use this app well!"
}