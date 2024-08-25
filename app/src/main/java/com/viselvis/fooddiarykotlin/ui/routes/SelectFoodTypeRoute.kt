package com.viselvis.fooddiarykotlin.ui.routes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.database.SelectFoodTypeModel
import com.viselvis.fooddiarykotlin.utils.BaseClickableCard
import com.viselvis.fooddiarykotlin.utils.FoodTypeItemClickable

@Composable
fun SelectFoodTypeRoute(
    navigateToAddFoodItem: (Int) -> Unit,
) {
    val initialList = listOf (
        SelectFoodTypeModel(
            0,
            "Food",
            "add food item",
            R.drawable.icon_food_50
        ),
        SelectFoodTypeModel(
            1,
            "Medicine",
            "add medicine item",
            R.drawable.icon_medicine_50
        )
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
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(15.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(initialList) { item ->
                    FoodTypeItemClickable(
                        isFixedHeight = false,
                        clickable = {
                            when (item.foodTypeId) {
                                1 -> navigateToAddFoodItem(1)
                                else -> navigateToAddFoodItem(0)
                            }
                        },
                        name = item.foodTypeTitle,
                        iconId = item.drawableResource,
                        contentDesc = item.contentDescription,
                    )
                }
            }
        }
    }
}