package com.viselvis.fooddiarykotlin.ui.routes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import androidx.navigation.NavHostController
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.ui.NoteEatDestinations
import com.viselvis.fooddiarykotlin.ui.theme.md_theme_light_primary
import com.viselvis.fooddiarykotlin.utils.BaseChip
import com.viselvis.fooddiarykotlin.utils.BaseDialog
import com.viselvis.fooddiarykotlin.utils.BaseItemClickable
import com.viselvis.fooddiarykotlin.utils.BaseTextField
import com.viselvis.fooddiarykotlin.utils.BaseTextFieldWithoutBg
import com.viselvis.fooddiarykotlin.utils.FlowRow
import com.viselvis.fooddiarykotlin.viewmodels.ItemDetailUiState
import com.viselvis.fooddiarykotlin.viewmodels.ItemDetailViewModel

// @Preview(widthDp = 412, heightDp = 892)
@Composable
fun ItemDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: ItemDetailViewModel,
    foodItemId: Long?,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is ItemDetailUiState.Error -> ItemDetailError(errorMessage = state.errorMessage)
        is ItemDetailUiState.Loading -> {
            ItemDetailLoading()
            foodItemId?.let { viewModel.fetchFoodItem(it) }
        }
        is ItemDetailUiState.ViewMode -> ItemDetailPage(
            state,
            modifier
        ) {
            if (foodItemId != null) {
                viewModel.editFoodItem(foodItemId)
            }
        }

        is ItemDetailUiState.EditMode -> ItemEditPage(
            state,
            modifier,
            insertItemIngredientInput = { viewModel.insertItemIngredientInput(it) },
            deleteItemIngredient = { viewModel.removeInIngredientList(it) },
            insertIngredient = { viewModel.insertIngredient(it) },
            updateItemName = { viewModel.updateItemName(it) },
            updateItemDescription = { viewModel.updateItemDetail(it) },
            navController = navController,
            updateFoodItem = { viewModel.updateFoodItem() }
        )
    }
}

@Composable
fun ItemDetailError(
    modifier: Modifier = Modifier,
    errorMessage: String?
) {
    Surface(modifier.fillMaxSize()) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            if (errorMessage != null) {
                Text(errorMessage)
            }
        }
    }
}

@Composable
fun ItemDetailLoading(modifier: Modifier = Modifier) {
    Surface(modifier.fillMaxSize()) {
        Box {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun ItemDetailPage(
    state: ItemDetailUiState.ViewMode,
    modifier: Modifier,
    toEditMode: () -> Unit,
) {
    Surface (
        modifier = modifier.fillMaxSize()
    ) {
        Scaffold (
            content = { padding ->
                Column (
                    modifier = modifier.padding(
                        PaddingValues(
                            start = 15.dp,
                            end = 15.dp,
                            bottom = padding.calculateBottomPadding()
                        )
                    ).fillMaxHeight()
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
                    Text (
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = state.itemDetailToDisplay.foodItemTitle,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = state.itemDetailToDisplay.foodItemDetails
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Contains: ",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    FlowRow(
                        horizontalGap = 8.dp,
                        verticalGap = 8.dp
                    ) {
                        for (ingredient in state.itemDetailToDisplay.foodItemIngredients) {
                            BaseChip(
                                text = ingredient
                            )
                        }
                    }
                }
            },
            bottomBar = {
                Row (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BaseItemClickable(
                        clickable = toEditMode,
                        name = "Edit food item",
                        iconId = R.drawable.baseline_edit_24
                    )
                }
            }
        )
    }
}

@Composable
fun ItemEditPage(
    state: ItemDetailUiState.EditMode,
    modifier: Modifier,
    insertIngredient: (String) -> Unit,
    insertItemIngredientInput: (String) -> Unit,
    updateItemName: (String) -> Unit,
    updateItemDescription: (String) -> Unit,
    deleteItemIngredient: (String) -> Unit,
    navController: NavHostController,
    updateFoodItem: () -> Unit
) {
    var isItemAddedDialogShown by mutableStateOf(false)
    if (!isItemAddedDialogShown && (state.itemToEdit.isDataInserted != (-1).toLong())) {
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

    Surface (modifier = Modifier.fillMaxSize()) {
        Scaffold (
            bottomBar = {
                Row (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BaseItemClickable(
                        clickable = updateFoodItem,
                        name = "Save food item",
                        iconId = R.drawable.baseline_save_24
                    )
                }
            }
        ) {
            innerPadding ->
            Column(
                modifier = modifier.padding(
                    PaddingValues(
                        start = 15.dp,
                        end = 15.dp,
                        bottom = innerPadding.calculateBottomPadding()
                    )
                )
                .fillMaxSize()
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
//            Text (
//                modifier = Modifier.align(Alignment.CenterHorizontally),
//                text = state.itemToEdit.itemName,
//                style = MaterialTheme.typography.headlineSmall
//            )
                BaseTextFieldWithoutBg(
                    text = state.itemToEdit.itemName,
                    onTextChanged = {
                        updateItemName(it)
//                    if (itemNameToEdit.isNotEmpty()) {
//                        addFoodItemViewModel.errorMessage = ""
//                    }
                    },
                    placeholderText = if (state.itemToEdit.itemFoodType == 1) {
                        stringResource(id = R.string.meds_item_name)
                    } else {
                        stringResource(id = R.string.food_item_name_not_italic)
                    },
                )
                if (state.itemToEdit.errorMessage.isNotEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text (
                            text = state.itemToEdit.errorMessage,
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Normal,
                            color = Color.Red,
                            textAlign = TextAlign.Start
                        )
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                BaseTextFieldWithoutBg(
                    text = state.itemToEdit.itemDetail,
                    onTextChanged = {
                        updateItemDescription(it)
//                    if (itemNameToEdit.isNotEmpty()) {
//                        addFoodItemViewModel.errorMessage = ""
//                    }
                    },
                    placeholderText = if (state.itemToEdit.itemFoodType == 1) {
                        stringResource(id = R.string.meds_item_details)
                    } else {
                        stringResource(id = R.string.food_item_details)
                    },
                )
//            Text(
//                text = state.itemToEdit.itemDetail
//            )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Contains: ",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(5.dp))

                when (state.itemToEdit.itemFoodType) {
                    0 -> {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            FlowRow(
                                horizontalGap = 8.dp,
                                verticalGap = 8.dp
                            ) {
                                for (ingredient in state.itemToEdit.itemIngredientsList) {
                                    BaseChip(
                                        text = ingredient,
                                        clickable = {
                                            deleteItemIngredient(ingredient)
                                        }
                                    )
                                }

                                BaseTextField(
                                    givenModifier = Modifier
                                        .fillMaxWidth()
                                        .onKeyEvent { event ->
                                            if (event.key == Key.Enter) {
                                                val input = state.itemToEdit.itemIngredientInput

                                                if (input.isNotEmpty()) {
                                                    insertIngredient(input)
                                                }
                                                true
                                            }
                                            false
                                        },
                                    text = state.itemToEdit.itemIngredientInput,
                                    onTextChanged = { insertItemIngredientInput(it) },
                                    placeholderText = stringResource(R.string.ingredients_hint),
                                    isSingleLine = true
                                )
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}
