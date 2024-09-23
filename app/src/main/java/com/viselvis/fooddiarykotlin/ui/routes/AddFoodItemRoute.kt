package com.viselvis.fooddiarykotlin.ui.routes

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.ui.NoteEatDestinations
import com.viselvis.fooddiarykotlin.ui.theme.NoteEatTheme
import com.viselvis.fooddiarykotlin.ui.theme.md_theme_light_primary
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
            Scaffold(
                bottomBar = {
                    Row (
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        BaseItemClickable(
                            clickable = { foodItemType?.let { viewModel.insertFoodItemOnDb(it) } },
                            name = if (foodItemType == 1) {
                                    stringResource(id = R.string.add_meds_intake)
                                } else {
                                stringResource(id = R.string.add_food_item)
                            },
                            iconId = R.drawable.baseline_save_24
                        )
                    }
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier.padding(
                        PaddingValues(
                            start = 15.dp,
                            end = 15.dp,
                            bottom = innerPadding.calculateBottomPadding()
                        )
                    ).fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .drawWithCache {
                                val roundedPolygon = RoundedPolygon(
                                    numVertices = 6,
                                    radius = size.minDimension / 2,
                                    centerX = size.width / 2,
                                    centerY = size.height / 2
                                )
                                val roundedPolygonPath = roundedPolygon
                                    .toPath()
                                    .asComposePath()
                                onDrawBehind {
                                    drawPath(roundedPolygonPath, color = md_theme_light_primary)
                                }
                            }
                            .size(50.dp, 50.dp)
                            .align(alignment = Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(5.dp))

                    BaseTextFieldWithoutBg(
                        text = uiState.itemName,
                        onTextChanged = {
                            viewModel.updateItemName(it)
                        },
                        placeholderText = if (foodItemType == 1) {
                            stringResource(id = R.string.meds_item_name)
                        } else {
                            stringResource(id = R.string.food_item_name)
                        },
                        inputTextStyle = MaterialTheme.typography.headlineSmall,
                        isTextCenter = true
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

                    Spacer(modifier = Modifier.height(5.dp))

                    BaseTextFieldWithoutBg(
                        text = uiState.itemDetail,
                        onTextChanged = {
                            viewModel.updateItemDetail(it)
                        },
                        placeholderText = if (foodItemType == 1) {
                            stringResource(id = R.string.meds_item_details)
                        } else {
                            stringResource(id = R.string.food_item_details)
                        },
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    when (foodItemType) {
                        0 -> {
                            Text(
                                text = "Contains: ",
                                style = MaterialTheme.typography.headlineSmall
                            )
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

//                    Button(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(5.dp),
//                        shape = RoundedCornerShape(45.dp),
//                        onClick = {
//                            foodItemType?.let { viewModel.insertFoodItemOnDb(it) }
//                        }
//                    ) {
//                        Text(
//                            text = if (foodItemType == 1) {
//                                stringResource(id = R.string.add_meds_intake)
//                            } else {
//                                stringResource(id = R.string.add_food_item)
//                            }
//                        )
//                    }
                }
            }

        }
    }
}