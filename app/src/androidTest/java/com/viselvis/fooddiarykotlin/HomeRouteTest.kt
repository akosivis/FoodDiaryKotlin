package com.viselvis.fooddiarykotlin

import androidx.compose.ui.test.junit4.createComposeRule
import com.viselvis.fooddiarykotlin.utils.BaseClickableCard
import org.junit.Rule
import org.junit.Test

class HomeRouteTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun viewFoodHistoryButtonTest() {
        composeTestRule.setContent {
            BaseClickableCard(
                clickable = {},
                name = "View food history"
            )
        }
        Thread.sleep(5000)
    }
}