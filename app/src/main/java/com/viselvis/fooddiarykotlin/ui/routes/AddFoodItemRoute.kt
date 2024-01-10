package com.viselvis.fooddiarykotlin.ui.routes

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
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
    val foodItemType by mutableStateOf(type)
    Log.d(TAG, "AddFoodItemRoute: foodItemType passed is $foodItemType")
    val isInsertedSuccessfully = viewModel.isDataInserted.observeAsState(-1)
    var isItemAddedDialogShown by mutableStateOf(false)
    if (!isItemAddedDialogShown && (isInsertedSuccessfully.value != (-1).toLong())) {
        BaseDialog(
            onDismiss = {
                isItemAddedDialogShown = true
                navController.popBackStack(
                    route = NoteEatDestinations.HOME_ROUTE,
                    inclusive = false
                )
            },
            message = "Item is added successfully!"
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
                    text = viewModel.itemName,
                    onTextChanged = {
                        viewModel.itemName = it
                        if (viewModel.itemName.isNotEmpty()) {
                            viewModel.errorMessage = ""
                        }
                    },
                    placeholderText = if (foodItemType == 1) {
                        "Medicine item name"
                    } else {
                        "Food item name"
                    },
                )

                if (viewModel.errorMessage.isNotEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = viewModel.errorMessage,
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Normal,
                            color = Color.Red,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                BaseTextField(
                    text = viewModel.itemDetail,
                    onTextChanged = { viewModel.itemDetail = it },
                    placeholderText = if (foodItemType == 1) {
                        "Medicine item details"
                    } else {
                        "Food item details"
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))
                when (foodItemType) {
                    0 -> {
                        Text("Contains: ")
                        Spacer(modifier = Modifier.height(5.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FlowRow(
                                horizontalGap = 8.dp,
                                verticalGap = 8.dp,
                            ) {
                                for (ingredient in viewModel.itemIngredientsList) {
                                    BaseChip(
                                        text = ingredient,
                                        clickable = {
                                            viewModel.itemIngredientsList.remove(ingredient)
                                        }
                                    )
                                }

                                BaseTextField(
                                    givenModifier = Modifier
                                        .fillMaxWidth()
                                        .onKeyEvent { event ->
                                            if (event.key == Key.Enter) {
                                                val input =
                                                    viewModel.itemIngredientInput
                                                if (input.isNotEmpty()) {
                                                    viewModel.insertIngredient(input)
                                                }
                                                true
                                            }
                                            false
                                        },
                                    text = viewModel.itemIngredientInput,
                                    onTextChanged = {
                                        viewModel.itemIngredientInput = it
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
                            "Add medicine intake"
                        } else {
                            "Add food item"
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