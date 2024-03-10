package com.viselvis.fooddiarykotlin.ui.routes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.utils.BaseClickableCard
import com.viselvis.fooddiarykotlin.utils.BaseColumnItem
import com.viselvis.fooddiarykotlin.viewmodels.HomeRouteState
import com.viselvis.fooddiarykotlin.viewmodels.MainViewModel

private enum class HomeScreenType {
    Walkthrough,
    MainContent
}

@Composable
fun HomeRoute(
    viewModel: MainViewModel,
    navigateToSelectFoodType: () -> Unit,
    navigateToFoodHistory: () -> Unit,
    navigateToEnterNameRoute: () -> Unit
){
    // val recentFoodItems: List<FoodItemModel> by viewModel.latestFoodItems.observeAsState(initial = listOf())
    // val userNameState by viewModel.userNameState.collectAsState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeRoute(
        uiState = uiState,
        navToSelectFoodType = navigateToSelectFoodType,
        navToFoodHistory = navigateToFoodHistory,
        navToEnterNameRoute = navigateToEnterNameRoute
    )

    if (!userNameState.isThereUserName) {
        navigateToEnterNameRoute()
    }

    Surface (
        modifier = Modifier.fillMaxSize()
    ) {
        Column (modifier = Modifier.padding(15.dp)){
            Text(
                text ="Hi ${userNameState.userName}",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Here are your latest food items: ",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(15.dp))

            if (recentFoodItems.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(recentFoodItems) { item ->
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
                DisplayNoItems()
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BaseClickableCard (
                    modifierAddtl = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    clickable = { navigateToSelectFoodType() },
                    name = "Add food item",
                )
                Spacer(modifier = Modifier.width(15.dp))
                BaseClickableCard (
                    modifierAddtl = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    clickable = { navigateToFoodHistory() },
                    name = "View food history"
                )
            }
        }
    }
}

@Composable
fun HomeRoute(
    uiState: HomeRouteState,
    navigateToSelectFoodType: () -> Unit,
    navigateToFoodHistory: () -> Unit,
    navigateToEnterNameRoute: () -> Unit
) {

}

@Composable
fun DisplayNoItems() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp)) {
        Text (
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(id = R.string.no_items_to_display),
            textAlign = TextAlign.Center
        )
    }
}

