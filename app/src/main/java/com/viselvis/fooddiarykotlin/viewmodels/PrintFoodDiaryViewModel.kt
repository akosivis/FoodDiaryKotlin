package com.viselvis.fooddiarykotlin.viewmodels

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.database.FoodItemRepository
import com.viselvis.fooddiarykotlin.utils.areDatesTheSame
import com.viselvis.fooddiarykotlin.utils.convertDateToFileName
import com.viselvis.fooddiarykotlin.utils.convertDateToText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.*


data class PrintFoodDiaryUiState(
    val showDatePicker : Boolean = false,
    val dateToday: Calendar = Calendar.getInstance(),
    val dateDisplayClicked: Int = -1,
    val allowGeneratePDF: Boolean = true,
    val itemsToPrint: List<FoodItemModel> = emptyList(),
    val toastMessage: String = "",
    val isLoading: Boolean = false,
)

class PrintFoodDiaryViewModel(private val repo: FoodItemRepository): ViewModel() {
    private val _uiState = MutableStateFlow(PrintFoodDiaryUiState())
    val uiState: StateFlow<PrintFoodDiaryUiState> = _uiState.asStateFlow()

    fun longToStringDisplay(date: Long): CharSequence {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date

        val mMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
        val mDay = calendar[Calendar.DAY_OF_MONTH]
        val mYear = calendar[Calendar.YEAR]

        return "$mDay $mMonth $mYear"
    }

    fun getFoodItems(fromDate: Long, toDate: Long, context: Context) {
        updateLoading(true)
        Log.d(TAG, "getFoodItems: $fromDate and $toDate")
        viewModelScope.launch(Dispatchers.IO) {
            repo.getFoodItemsOnGivenDate(fromDate, toDate).apply {
                if (this.isNotEmpty()) {
                    updateFoodItems(this)
                    renderToPdf(fromDate, toDate, context)
                } else {
                    updateToastMessage("There's no items to print for the given date range")
                }
            }
            updateLoading(false)
        }
    }

    private fun updateFoodItems(foodItemModels: List<FoodItemModel>) {
        _uiState.update {
            it.copy(
                itemsToPrint = foodItemModels
            )
        }
    }

    private fun updateToastMessage(msg: String) {
        _uiState.update {
            it.copy(
                toastMessage = msg
            )
        }
    }

    private fun updateLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(isLoading = isLoading)
        }
    }

    fun showDatePicker(showPicker: Boolean) {
        _uiState.update {
            it.copy(
                showDatePicker = showPicker
            )
        }
    }

    fun updateDateDisplayClicked(dateInput: Int) {
        _uiState.update {
            it.copy(
                dateDisplayClicked = dateInput
            )
        }
    }

    private fun renderToPdf(
        fromDate: Long,
        toDate: Long,
        context: Context
    ) {
        val pageHeight = 792
        val pageWidth = 612
        val pdfDocument = PdfDocument()
        var dateToPrint: Date? = null
        var baseY = 150F

        val title = Paint()
        val printFoodItem = Paint()
        val printIngredients = Paint()
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

        val myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)
        var canvas: Canvas = myPage.canvas
        val xPosCentered = canvas.width / 2F
        drawTitle(title, canvas, xPosCentered, fromDate, toDate)

        for (item in _uiState.value.itemsToPrint) {
            if (baseY >= 660F) {
                pdfDocument.finishPage(myPage)
                myPage = pdfDocument.startPage(myPageInfo)
                canvas = myPage.canvas
                baseY = 150F

                title.textAlign = Paint.Align.CENTER
                drawTitle(title, canvas, xPosCentered, fromDate, toDate)
            }

            title.textAlign = Paint.Align.LEFT
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
        pdfDocument.finishPage(myPage)

        val file = File(context.getExternalFilesDir(null)?.absolutePath,
            "FoodDiary-${convertDateToFileName(Date(Calendar.getInstance().timeInMillis))}.pdf")

        updateToastMessage(
            try {
                pdfDocument.writeTo(FileOutputStream(file))
                "PDF file generated in ${context.getExternalFilesDir(null)?.absolutePath}"
            } catch (e: Exception) {
                e.printStackTrace()
                "Failed to generate PDF file.."
            }
        )

        pdfDocument.close()
    }

    private fun drawTitle(
        title: Paint,
        canvas: Canvas,
        xPosCentered: Float,
        fromDate: Long,
        toDate: Long
    ) {
        canvas.drawText("Food Diary", xPosCentered, 90F, title)
        canvas.drawText("Records from ${convertDateToText(Date(fromDate))} " +
                "to ${convertDateToText(Date(toDate))}", xPosCentered, 110F, title)
    }

    fun emptyToastMessage() {
        updateToastMessage("")
    }

    companion object {
        private const val TAG = "PrintFoodDiaryViewModel"
    }
}

class PrintFoodDiaryViewModelFactory(private val repo: FoodItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrintFoodDiaryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PrintFoodDiaryViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}