package com.viselvis.fooddiarykotlin.ui.routes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viselvis.fooddiarykotlin.database.FoodTypeModel
import com.viselvis.fooddiarykotlin.utils.BaseClickableCard

@Composable
fun SelectFoodTypeRoute(
    navigateToAddFoodItem: Unit,
) {
    val initialList = listOf (
        FoodTypeModel(0,"Add food item", ""),
        FoodTypeModel(1, "Add medicine taken", "")
    )

    Surface (modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Text(text = "Select food type:", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                items(initialList) { item ->
                    BaseClickableCard(
                        clickable = {},
                        name = item.foodTypeTitle,
                    )
                }
            }
        }
    }
}