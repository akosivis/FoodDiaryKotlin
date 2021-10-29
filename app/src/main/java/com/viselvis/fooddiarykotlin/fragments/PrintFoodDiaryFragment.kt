package com.viselvis.fooddiarykotlin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.application.FoodItemListApplication
import com.viselvis.fooddiarykotlin.databinding.FragmentPrintBinding
import com.viselvis.fooddiarykotlin.viewmodels.AddFoodItemViewModel
import com.viselvis.fooddiarykotlin.viewmodels.AddFoodItemViewModelFactory
import com.viselvis.fooddiarykotlin.viewmodels.PrintFoodDiaryViewModel
import com.viselvis.fooddiarykotlin.viewmodels.PrintFoodDiaryViewModelFactory
import kotlinx.coroutines.launch
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*

class PrintFoodDiaryFragment : Fragment() {

    private val TAG = "PrintFoodDiaryFragment"
    private lateinit var binding: FragmentPrintBinding
    private lateinit var pickerBuilder : MaterialDatePicker.Builder<Pair<Long, Long>>
        // (MaterialDatePicker.Builder<Pair<Long, Long>>)()
    //    MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();
    private lateinit var picker : MaterialDatePicker<Pair<Long, Long>>
    private val printFoodDiaryViewModel: PrintFoodDiaryViewModel by viewModels {
        PrintFoodDiaryViewModelFactory((activity?.application as FoodItemListApplication).repository)
    }
    private var fromDateSelected: Long? = null
    private var toDateSelected: Long? = null
    private var foodItemListByRange: List<FoodItemModel> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_print, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_print, container, false)

        initializePicker()
//        initializeObserver()

        binding.btnSelectDate.setOnClickListener {
            showDateRangePicker()
        }

        binding.btnGeneratePdf.setOnClickListener {
            getAllFoodItemsFromGivenRange()
        }

        return binding.root;
    }

//    private fun initializeObserver() {
//        printFoodDiaryViewModel.foodItemsByRange.observe(viewLifecycleOwner, Observer { isSuccess ->
//            if (!isSuccess.equals(-1)) {
//                Toast.makeText(context, "There are ${printFoodDiaryViewModel.foodItemsByRange.value?.size ?: 0} items", Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(context, "Insertion is not successful", Toast.LENGTH_LONG).show()
//            }
//        })
//    }

    private fun getAllFoodItemsFromGivenRange() {
        if (fromDateSelected != null && toDateSelected != null) {
            lifecycle.coroutineScope.launch {
                printFoodDiaryViewModel.getFoodItemsByRange(fromDateSelected!!, toDateSelected!!).collect {
                    foodItemListByRange = it

                    for (i in foodItemListByRange) {
                        Log.d(TAG, "Food item: ${i.foodItemTitle}")
                    }
                    // Toast.makeText(activity, "Size is ${foodItemListByRange.size}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "Problem!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializePicker() {
        pickerBuilder = MaterialDatePicker.Builder.dateRangePicker()
        val now = Calendar.getInstance()
        pickerBuilder.setSelection(Pair(now.timeInMillis, now.timeInMillis))
        pickerBuilder.setTheme(R.style.DateRangePickerCustomTheme)

        picker = pickerBuilder.build()
        picker.addOnNegativeButtonClickListener { picker.dismiss() }
        picker.addOnPositiveButtonClickListener {
            setSelectedDates(it.first, it.second)
        }
    }

    private fun setSelectedDates(fromDate: Long?, toDate: Long?) {

        // val calendar = Calendar.getInstance()
        // calendar.time = Date() // Set your date object here

//        calendar.set(Calendar.HOUR_OF_DAY, 23)
//        calendar.set(Calendar.MINUTE, 59)
//        calendar.set(Calendar.SECOND, 59)
//        calendar.time // Your changed date o

        val calendar = Calendar.getInstance()
        calendar.time = toDate?.let { Date(it) }
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.time

//        val toDateEditHours = toDate?.let { Date(it) }
//        toDateEditHours.let {
//            it
//        }set(Calendar.HOUR_OF_DAY, 23)
//        toDateEditHours.set(Calendar.MINUTE, 59)
//        toDateEditHours.set(Calendar.SECOND, 59)
//        calendar.time


        fromDateSelected = fromDate
        toDateSelected = calendar.timeInMillis
        // toDateSelected = toDate?.let { Date(it) }

        if (fromDate != null) {
            binding.tvFromDate.text = longToStringDisplay(fromDate)
        }
        if (toDate != null) {
            binding.tvToDate.text = longToStringDisplay(toDate)
        }

//        this.lifecycleScope.launch {
//            viewMo
//        }
    }

    private fun longToStringDisplay(date: Long): CharSequence {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date

        val mMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
        val mDay = calendar[Calendar.DAY_OF_MONTH]

        return "$mDay $mMonth"
    }

    private fun showDateRangePicker() {
        picker.show(activity?.supportFragmentManager!!, picker.toString())
    }

    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return df.parse(date).time
    }

    companion object {

    }
}