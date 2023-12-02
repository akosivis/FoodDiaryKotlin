package com.viselvis.fooddiarykotlin.fragments

import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.application.FoodItemListApplication
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.databinding.FragmentPrintBinding
import com.viselvis.fooddiarykotlin.utils.areDatesTheSame
import com.viselvis.fooddiarykotlin.utils.convertDateToText
import com.viselvis.fooddiarykotlin.viewmodels.PrintFoodDiaryViewModel
import com.viselvis.fooddiarykotlin.viewmodels.PrintFoodDiaryViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.properties.Delegates

class PrintFoodDiaryFragment : Fragment() {

    private val TAG = "PrintFoodDiaryFragment"
    private lateinit var binding: FragmentPrintBinding
    private lateinit var pickerBuilder : MaterialDatePicker.Builder<Pair<Long, Long>>
    private lateinit var picker : MaterialDatePicker<Pair<Long, Long>>
    private val printFoodDiaryViewModel: PrintFoodDiaryViewModel by viewModels {
        PrintFoodDiaryViewModelFactory((activity?.application as FoodItemListApplication).repository)
    }
    private var foodItemListByRange: List<FoodItemModel> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_print, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_print, container, false)

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.apply {
            setContent {
                PrintFoodDiaryPage()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PrintFoodDiaryPage() {
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

        if (printFoodDiaryViewModel.itemsToPrint.isNotEmpty()) {
            renderPdf(fromDateSelected, toDateSelected, context)
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

        if (printFoodDiaryViewModel.isPrintListEmpty) {
            Toast.makeText(context, "There's no items to print with the given dates", Toast.LENGTH_LONG).show()
            printFoodDiaryViewModel.isPrintListEmpty = false
        }

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
                        toDateSelected
                    )
                    // TODO: Show loading screen
                },
                shape = RoundedCornerShape(45.dp),
            ) {
                Text("Generate PDF", fontSize = 20.sp)
            }
        }
    }

    private fun renderPdf(
        fromDate: Long,
        toDate: Long,
        context: Context
    ) {
        val pageHeight = 792
        val pageWidth = 612
        var pdfDocument = PdfDocument()
        var dateToPrint: Date? = null

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        var paint: Paint = Paint()
        var title: Paint = Paint()
        var printFoodItem = Paint()
        var printIngredients = Paint()
        var baseY = 150F
        var myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

        // below line is used for setting
        // start page for our PDF file.
        var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        var canvas: Canvas = myPage.canvas
        val xPosCentered = canvas.width / 2F
        title.apply {
            this.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            this.textSize = 20F
            this.color = ContextCompat.getColor(context, R.color.black)
            this.textAlign = Paint.Align.CENTER
        }
        printFoodItem.apply {
            this.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            this.textSize = 20F
            this.color = ContextCompat.getColor(context, R.color.black)
            this.textAlign = Paint.Align.LEFT
        }
        printIngredients.apply {
            this.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            this.textSize = 15F
            this.color = ContextCompat.getColor(context, R.color.black)
            this.textAlign = Paint.Align.LEFT
        }

        canvas.drawText("Food Diary", xPosCentered, 90F, title)
        canvas.drawText("Records from ${convertDateToText(Date(fromDate))} " +
                "to ${convertDateToText(Date(toDate))}", xPosCentered, 110F, title)

        title.textAlign = Paint.Align.LEFT
        for (item in printFoodDiaryViewModel.itemsToPrint) {
            Log.d(TAG, "renderPdf() item to render is ${item.foodItemTitle}")
            if (!areDatesTheSame(dateToPrint, item.foodItemLastModified)) {
                dateToPrint = item.foodItemLastModified
                canvas.drawText(convertDateToText(item.foodItemLastModified), 25F, baseY, title)
                baseY += 25F
            }

            canvas.drawText(convertDateToText(item.foodItemLastModified, 1), 60F, baseY, printFoodItem)
            canvas.drawText(item.foodItemTitle, 140F, baseY, printFoodItem)
            if (item.foodItemIngredients.isNotEmpty()) {
                baseY += 20F
                canvas.drawText("contains ${item.foodItemIngredients.joinToString(", ")}", 140F, baseY, printIngredients)
            }
            baseY += 25F
        }

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage)

        // below line is used to set the name of
        // our PDF file and its path.
        val file = File(context.getExternalFilesDir(null)?.absolutePath, "FoodDiary-${Calendar.getInstance().timeInMillis}.pdf")

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(FileOutputStream(file))

            // on below line we are displaying a toast message as PDF file generated..
            Toast.makeText(context, "PDF file generated in ${context.getExternalFilesDir(null)?.absolutePath}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // below line is used
            // to handle error
            e.printStackTrace()

            // on below line we are displaying a toast message as fail to generate PDF
            Toast.makeText(context, "Fail to generate PDF file..", Toast.LENGTH_SHORT)
                .show()
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close()
    }

    companion object {

    }
}