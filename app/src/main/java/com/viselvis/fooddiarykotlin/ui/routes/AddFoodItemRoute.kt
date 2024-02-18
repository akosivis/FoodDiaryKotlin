package com.viselvis.fooddiarykotlin.ui.routes

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.ui.NoteEatDestinations
import com.viselvis.fooddiarykotlin.ui.theme.NoteEatTheme
import com.viselvis.fooddiarykotlin.utils.*
import com.viselvis.fooddiarykotlin.viewmodels.AddFoodItemViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddFoodItemRoute(
    navController: NavHostController,
    viewModel: AddFoodItemViewModel,
    type: Int?
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val foodItemType by mutableStateOf(type)
    Log.d(TAG, "AddFoodItemRoute: foodItemType passed is $foodItemType")

    var isItemAddedDialogShown by mutableStateOf(false)
    if (!isItemAddedDialogShown && (uiState.isDataInserted != (-1).toLong())) {
        BaseDialog(
            onDismiss = {
                isItemAddedDialogShown = true
                navController.popBackStack(
                    route = NoteEatDestinations.HOME_ROUTE,
                    inclusive = false
                )
            },
            message = stringResource(id = R.string.item_added_successfully)
        )
    }

    NoteEatTheme {
        Surface (modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                BaseTextField(
                    text = uiState.itemName,
                    onTextChanged = {
                        viewModel.updateItemName(it)
                    },
                    placeholderText = if (foodItemType == 1) {
                        stringResource(id = R.string.meds_item_name)
                    } else {
                        stringResource(id = R.string.food_item_name)
                    },
                )

                if (uiState.errorMessage.isNotEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text (
                            text = uiState.errorMessage,
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Normal,
                            color = Color.Red,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                BaseTextField(
                    text = uiState.itemDetail,
                    onTextChanged = { viewModel.updateItemDetail(it) },
                    placeholderText = if (foodItemType == 1) {
                        stringResource(id = R.string.meds_item_details)
                    } else {
                        stringResource(id = R.string.food_item_details)
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))
                when (foodItemType) {
                    0 -> {
                        Text(text = stringResource(id = R.string.contains))
                        Spacer(modifier = Modifier.height(5.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FlowRow(
                                horizontalGap = 8.dp,
                                verticalGap = 8.dp,
                            ) {
                                for (ingredient in uiState.itemIngredientsList) {
                                    BaseChip(
                                        text = ingredient,
                                        clickable = {
                                            viewModel.removeInIngredientList(ingredient)
                                        }
                                    )
                                }

                                BaseTextField(
                                    givenModifier = Modifier
                                        .fillMaxWidth()
                                        .onKeyEvent { event ->
                                            if (event.key == Key.Enter) {
                                                val input =
                                                    uiState.itemIngredientInput
                                                if (input.isNotEmpty()) {
                                                    viewModel.insertIngredient(input)
                                                }
                                                true
                                            }
                                            false
                                        },
                                    text = uiState.itemIngredientInput,
                                    onTextChanged = {
                                        viewModel.insertItemIngredientInput(it)
                                    },
                                    placeholderText = stringResource(R.string.ingredients_hint),
                                    isSingleLine = true
                                )
                            }
                        }
                    }
                    else -> {}
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    shape = RoundedCornerShape(45.dp),
                    onClick = {
                        foodItemType?.let { viewModel.insertFoodItemOnDb(it) }
                    }
                ) {
                    Text(
                        text = if (foodItemType == 1) {
                            stringResource(id = R.string.add_meds_intake)
                        } else {
                            stringResource(id = R.string.add_food_item)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AddFoodItemScreen(
    navController: NavHostController,
    viewModel: AddFoodItemViewModel,
    foodItemType: Int?,
    isInsertedSuccessfully: Long,
    dismiss: () -> Unit,
) {

}