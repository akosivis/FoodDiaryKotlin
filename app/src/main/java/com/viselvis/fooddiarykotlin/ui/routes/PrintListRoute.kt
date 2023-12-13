package com.viselvis.fooddiarykotlin.ui.routes

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.viselvis.fooddiarykotlin.utils.BaseLoadingIndicator
import com.viselvis.fooddiarykotlin.viewmodels.PrintFoodDiaryViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintListRoute(
    printFoodDiaryViewModel: PrintFoodDiaryViewModel
) {
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

    if (printFoodDiaryViewModel.toastMessage.isNotEmpty()) {
        Toast.makeText(context, printFoodDiaryViewModel.toastMessage, Toast.LENGTH_LONG).show()
        printFoodDiaryViewModel.emptyToastMessage()
    }

    if (printFoodDiaryViewModel.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                printFoodDiaryViewModel.showDatePicker = false
            },
            confirmButton = {
                TextButton(onClick = {
                    printFoodDiaryViewModel.showDatePicker = false
                    fromDateSelected = dateRangePickerState.selectedStartDateMillis!!
                    toDateSelected = dateRangePickerState.selectedEndDateMillis!!
                }) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    printFoodDiaryViewModel.showDatePicker = false
                }) {
                    Text(text = "Cancel")
                }
            }
        ) {
            DateRangePicker(
                state = dateRangePickerState
            )
        }
    }

    if (printFoodDiaryViewModel.isLoading) {
        BaseLoadingIndicator()
    }

    Surface (modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Select a date range ", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(40.dp))
            Row {
                Text(text = "from: ", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    modifier = Modifier
                        .alignByBaseline()
                        .clickable {
                            printFoodDiaryViewModel.showDatePicker = true
                            printFoodDiaryViewModel.dateDisplayClicked = 0
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
                            printFoodDiaryViewModel.showDatePicker = true
                            printFoodDiaryViewModel.dateDisplayClicked = 1
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
                enabled = printFoodDiaryViewModel.allowGeneratePDF,
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
                Text("Generate PDF", fontSize = 20.sp)
            }
        }
    }
}