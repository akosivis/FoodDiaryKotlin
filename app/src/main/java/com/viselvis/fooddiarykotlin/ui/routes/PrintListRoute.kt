package com.viselvis.fooddiarykotlin.ui.routes

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.utils.BaseLoadingIndicator
import com.viselvis.fooddiarykotlin.viewmodels.PrintFoodDiaryViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintListRoute(
    printFoodDiaryViewModel: PrintFoodDiaryViewModel
) {
    val uiState by printFoodDiaryViewModel.uiState.collectAsStateWithLifecycle()
    val calendar = Calendar.getInstance()
    val context = LocalContext.current

    var fromDateSelected by remember {
        mutableStateOf(calendar.timeInMillis) // or use mutableStateOf(calendar.timeInMillis)
    }
    var toDateSelected by remember {
        mutableStateOf(calendar.timeInMillis) // or use mutableStateOf(calendar.timeInMillis)
    }
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = fromDateSelected,
        initialSelectedEndDateMillis = toDateSelected
    )

    if (uiState.toastMessage.isNotEmpty()) {
        Toast.makeText(context, uiState.toastMessage, Toast.LENGTH_LONG).show()
        printFoodDiaryViewModel.emptyToastMessage()
    }

    if (uiState.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                printFoodDiaryViewModel.showDatePicker(false)
            },
            confirmButton = {
                TextButton(onClick = {
                    printFoodDiaryViewModel.showDatePicker( false)
                    fromDateSelected = dateRangePickerState.selectedStartDateMillis!!
                    toDateSelected = dateRangePickerState.selectedEndDateMillis!!
                }) {
                    Text(text = stringResource(id = R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    printFoodDiaryViewModel.showDatePicker(false)
                }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        ) {
            DateRangePicker(
                state = dateRangePickerState
            )
        }
    }

    if (uiState.isLoading) {
        BaseLoadingIndicator()
    }

    Surface (modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(stringResource(id = R.string.select_date_range), fontSize = 24.sp)
            Spacer(modifier = Modifier.height(40.dp))
            Row {
                Text(text = "from: ", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    modifier = Modifier
                        .alignByBaseline()
                        .clickable {
                            printFoodDiaryViewModel.showDatePicker(true)
                            printFoodDiaryViewModel.updateDateDisplayClicked(0)
                        },
                    text = printFoodDiaryViewModel.longToStringDisplay(fromDateSelected).toString(),
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(text = "to: ", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    modifier = Modifier
                        .alignByBaseline()
                        .clickable {
                            printFoodDiaryViewModel.showDatePicker(true)
                            printFoodDiaryViewModel.updateDateDisplayClicked(1)
                        },
                    text = printFoodDiaryViewModel.longToStringDisplay(toDateSelected).toString(),
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                enabled = uiState.allowGeneratePDF,
                onClick = {
                    printFoodDiaryViewModel.getFoodItems(
                        fromDateSelected,
                        toDateSelected,
                        context
                    )
                    // TODO: Show loading screen
                },
                shape = RoundedCornerShape(45.dp),
            ) {
                Text(stringResource(id = R.string.generate_pdf), fontSize = 20.sp)
            }
        }
    }
}