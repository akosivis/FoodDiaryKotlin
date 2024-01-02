package com.viselvis.fooddiarykotlin.ui.routes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viselvis.fooddiarykotlin.ui.theme.NoteEatTheme
import com.viselvis.fooddiarykotlin.viewmodels.FoodHistoryViewModel

@Composable
fun FoodHistoryPage(
    viewModel: FoodHistoryViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface (modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(8.dp)
                            .clickable {
                                viewModel.getPreviousDay() },
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Previous day"
                    )
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = viewModel.dateToDisplay,
                            fontSize = 16.sp
                        )
                        Text(
                            text = viewModel.dayToDisplay,
                            fontSize = 16.sp
                        )
                    }
                    if (uiState.givenCalendarInstance != viewModel.dateToday) {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(8.dp)
                                .clickable {
                                    viewModel.getNextDay() },
                            imageVector = Icons.Rounded.ArrowForward,
                            contentDescription = "Next day"
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            if (uiState.foodItems.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(uiState.foodItems) { foodItem ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = viewModel.getTimeFromLong(foodItem.foodItemCreated.time)
                                        .toString(),
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = foodItem.foodItemTitle,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Text(text = foodItem.foodItemDetails)
                                }
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "No food items recorded",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}