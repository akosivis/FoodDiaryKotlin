package com.viselvis.fooddiarykotlin.ui.routes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viselvis.fooddiarykotlin.database.SelectFoodTypeModel
import com.viselvis.fooddiarykotlin.utils.BaseClickableCard

@Composable
fun SelectFoodTypeRoute(
    navigateToAddFoodItem: Unit,
) {
    val initialList = listOf (
        SelectFoodTypeModel(0, "Add food item",
            Icons.Default.ShoppingCart, "add food item"),
        SelectFoodTypeModel(1, "Add medicine taken",
            Icons.Default.Menu, "add medicine item" )
    )

    Surface (
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Text(text = "Select food type:", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(initialList) { item ->
                    BaseClickableCard(
                        isFixedHeight = true,
                        clickable = {},
                        name = item.foodTypeTitle,
                        imageVector = item.foodTypeIcon,
                        contentDesc = item.contentDescription,
                    )
                }
            }
        }
    }
}