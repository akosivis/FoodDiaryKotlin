package com.viselvis.fooddiarykotlin

import android.app.Application
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.viselvis.fooddiarykotlin.application.FoodItemListApplication
import com.viselvis.fooddiarykotlin.utils.BaseClickableCard
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeRouteTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun initializeApp() {
        composeTestRule.launchApp(ApplicationProvider.getApplicationContext<FoodItemListApplication?>())
    }

    @Test
    fun app_launches() {
        composeTestRule.onNodeWithText("Here are your latest food items: ").assertExists()
    }

    @Test
    fun open_addItem() {
        composeTestRule.onNodeWithText("Add food item").performClick()
        composeTestRule.onNodeWithText("Select food type:").assertExists()
    }

    @Test
    fun open_viewFoodHistory() {
        composeTestRule.onNodeWithText("View food history").performClick()
        composeTestRule.onNodeWithContentDescription("Previous day").assertExists()
    }
//    @Test
//    fun viewAddFoodItemButtonTest() {
//        composeTestRule.setContent {
//            BaseClickableCard(
//                clickable = {},
//                name = "Add food item"
//            )
//        }
//        Thread.sleep(5000)
//    }
//
//    @Test
//    fun viewFoodHistoryButtonTest() {
//        composeTestRule.setContent {
//            BaseClickableCard(
//                clickable = {},
//                name = "View food history"
//            )
//        }
//        Thread.sleep(5000)
//    }
}