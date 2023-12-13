package com.viselvis.fooddiarykotlin.ui.routes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viselvis.fooddiarykotlin.utils.BaseClickableCard
import com.viselvis.fooddiarykotlin.utils.BaseColumnItem
import com.viselvis.fooddiarykotlin.viewmodels.MainViewModel

@Composable
fun HomeRoute(
    viewModel: MainViewModel
){
    val recentFoodItems by viewModel.allFoodItems.observeAsState()

    Surface (
        modifier = Modifier.fillMaxSize()
    ) {
        Column (modifier = Modifier.padding(15.dp)){
            Text("Hi User!")
            Text(text = "What have you eaten today?")
            Spacer(modifier = Modifier.height(15.dp))
            if (recentFoodItems != null) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(recentFoodItems!!) { item ->
                        BaseColumnItem(
                            itemType = 4,
                            content = {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = item.foodItemTitle,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        Text(text = item.foodItemDetails)
                                    }
                                }
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BaseClickableCard(
                    clickable = {
                        // listener.navigateToSelectFoodTypes()
                                },
                    name = "Add food item"
                )
                BaseClickableCard(clickable = {
                    // listener.navigateToFoodHistory()
                }, name = "View food history")
            }
        }
    }
}