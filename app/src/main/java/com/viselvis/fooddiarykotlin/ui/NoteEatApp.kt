package com.viselvis.fooddiarykotlin.ui

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.viselvis.fooddiarykotlin.application.FoodItemListApplication
import com.viselvis.fooddiarykotlin.ui.theme.NoteEatTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEatApp(
    app: FoodItemListApplication
) {
    NoteEatTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            NoteEatNavigationActions(navController)
        }
        val coroutineScope = rememberCoroutineScope()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: NoteEatDestinations.HOME_ROUTE
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Text("NoteEat")
                    Divider()
                    NavigationDrawerItem(
                        label = { Text("Home") },
                        selected = currentRoute == NoteEatDestinations.HOME_ROUTE,
                        onClick = {
                            navigationActions.navigateToHome
                            coroutineScope.launch {
                                drawerState.close()
                            }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Print") },
                        selected = currentRoute == NoteEatDestinations.PRINT_ROUTE,
                        onClick = {
                            navigationActions.navigateToPrintList
                            coroutineScope.launch {
                                drawerState.close()
                            }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Settings") },
                        selected = currentRoute == NoteEatDestinations.SETTINGS_ROUTE,
                        onClick = {
                            navigationActions.navigateToSettings
                            coroutineScope.launch {
                                drawerState.close()
                            }
                        }
                    )
                }
            },
            gesturesEnabled = true
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "AppBar") },
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
                },
            ) {
                NoteEatNavGraph(
                    application = app,
                    openDrawer = { coroutineScope.launch { drawerState.open() } }
                )
            }
        }
    }
}