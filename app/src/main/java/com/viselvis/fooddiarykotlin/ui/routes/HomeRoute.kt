package com.viselvis.fooddiarykotlin.ui.routes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viselvis.fooddiarykotlin.utils.BaseClickableCard
import com.viselvis.fooddiarykotlin.utils.BaseColumnItem
import com.viselvis.fooddiarykotlin.viewmodels.MainViewModel

@Composable
fun HomeRoute(
    viewModel: MainViewModel,
    navigateToSelectFoodType: () -> Unit,
    navigateToFoodHistory: () -> Unit,
    navigateToEnterNameRoute: () -> Unit
){
    val recentFoodItems by viewModel.uiState.collectAsState()
    val userNameState by viewModel.userNameState.collectAsState()

    if (!userNameState.isThereUserName) {
        navigateToEnterNameRoute()
    }

    Surface (
        modifier = Modifier.fillMaxSize()
    ) {
        Column (modifier = Modifier.padding(15.dp)){
            Text("Hi User!")
            Text(text = "Here are your latest food items: ")

            Spacer(modifier = Modifier.height(15.dp))

            if (recentFoodItems.latestFoodItems != null) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(recentFoodItems.latestFoodItems!!) { item ->
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
            } else {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)) {
                    Text(
                        "There are no items added yet!",
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BaseClickableCard (
                    clickable = { navigateToSelectFoodType() },
                    name = "Add food item",
                )
                BaseClickableCard(
                    clickable = { navigateToFoodHistory() },
                    name = "View food history"
                )
            }
        }
    }
}

