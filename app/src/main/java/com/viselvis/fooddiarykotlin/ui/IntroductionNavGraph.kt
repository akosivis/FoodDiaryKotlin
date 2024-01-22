package com.viselvis.fooddiarykotlin.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.viselvis.fooddiarykotlin.ui.routes.IntroductionRoute

@Composable
fun IntroductionNavGraph(
    modifier: Modifier,
    navController: NavHostController = rememberNavController(),
    startDest: String = NoteEatDestinations.INTRODUCTION_ROUTE
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDest
    ) {
        composable(NoteEatDestinations.INTRODUCTION_ROUTE) {
            IntroductionRoute()
        }
    }
}