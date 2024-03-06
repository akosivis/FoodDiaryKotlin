package com.viselvis.fooddiarykotlin.ui.routes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.database.SelectFoodTypeModel
import com.viselvis.fooddiarykotlin.utils.BaseClickableCard

@Composable
fun SelectFoodTypeRoute(
    navigateToAddFoodItem: (Int) -> Unit,
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
                .padding(15.dp)
        ) {
            Text(
                text = stringResource(id = R.string.select_food_type),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(15.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(initialList) { item ->
                    BaseClickableCard(
                        isFixedHeight = false,
                        clickable = {
                            when (item.foodTypeId) {
                                1 -> navigateToAddFoodItem(1)
                                else -> navigateToAddFoodItem(0)
                            }
                        },
                        name = item.foodTypeTitle,
                        imageVector = item.foodTypeIcon,
                        contentDesc = item.contentDescription,
                    )
                }
            }
        }
    }
}