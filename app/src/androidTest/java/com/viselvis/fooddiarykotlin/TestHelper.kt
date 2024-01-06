package com.viselvis.fooddiarykotlin

import android.content.Context
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.test.core.app.ApplicationProvider
import com.viselvis.fooddiarykotlin.application.FoodItemListApplication
import com.viselvis.fooddiarykotlin.ui.NoteEatApp

fun ComposeContentTestRule.launchApp(application: FoodItemListApplication) {
    // val application: FoodItemListApplication = (ApplicationProvider.getApplicationContext() as FoodItemListApplication)
    setContent {
        NoteEatApp(app = application)
    }
}