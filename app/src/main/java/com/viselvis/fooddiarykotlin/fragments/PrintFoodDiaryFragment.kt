package com.viselvis.fooddiarykotlin.fragments

import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
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
import com.viselvis.fooddiarykotlin.databinding.FragmentPrintBinding
import com.viselvis.fooddiarykotlin.viewmodels.PrintFoodDiaryViewModel
import com.viselvis.fooddiarykotlin.viewmodels.PrintFoodDiaryViewModelFactory
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import java.io.File
import java.io.FileOutputStream
import java.util.*

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
        savedInstanceState: Bundle?
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
            renderPdf(context)
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
                            printFoodDiaryViewModel.dateDisplayClicked = 1 },
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

    private fun renderPdf(context: Context) {
        // declaring width and height
        // for our PDF file.
        var pageHeight = 1120
        var pageWidth = 792

        // creating a bitmap variable
        // for storing our images
        lateinit var bmp: Bitmap
        lateinit var scaledbmp: Bitmap

        // creating an object variable
        // for our PDF document.
        var pdfDocument: PdfDocument = PdfDocument()

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        var paint: Paint = Paint()
        var title: Paint = Paint()
        var baseY = 150F

        // on below line we are initializing our bitmap and scaled bitmap.
        // bmp = BitmapFactory.decodeResource(context.resources, R.drawable.connector_shape)
        // scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false)


        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        var myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

        // below line is used for setting
        // start page for our PDF file.
        var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        var canvas: Canvas = myPage.canvas

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        // canvas.drawBitmap(scaledbmp, 56F, 40F, paint)

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.textSize = 15F

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.color = ContextCompat.getColor(context, R.color.purple_200)

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("A portal for IT professionals.", 209F, 100F, title)
        canvas.drawText("Geeks for Geeks", 209F, 80F, title)
        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        title.color = ContextCompat.getColor(context, R.color.purple_200)
        title.textSize = 15F

        // below line is used for setting
        // our text to center of PDF.
        title.textAlign = Paint.Align.CENTER
        for (i in printFoodDiaryViewModel.itemsToPrint) {
            canvas.drawText(i.foodItemTitle, 15F, baseY, title)
            canvas.drawText(i.foodItemTitle, 100F, baseY, title)
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