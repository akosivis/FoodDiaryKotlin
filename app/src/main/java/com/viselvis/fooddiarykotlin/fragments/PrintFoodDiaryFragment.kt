package com.viselvis.fooddiarykotlin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

//        binding.btnSelectDate.setOnClickListener {
//            showDateRangePicker()
//        }
//
//        binding.btnGeneratePdf.setOnClickListener {
//            getAllFoodItemsFromGivenRange()
//        }

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

//    private fun initializeObserver() {
//        printFoodDiaryViewModel.foodItemsByRange.observe(viewLifecycleOwner, Observer { isSuccess ->
//            if (!isSuccess.equals(-1)) {
//                Toast.makeText(context, "There are ${printFoodDiaryViewModel.foodItemsByRange.value?.size ?: 0} items", Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(context, "Insertion is not successful", Toast.LENGTH_LONG).show()
//            }
//        })
//    }

    private fun initializePicker() {
        pickerBuilder = MaterialDatePicker.Builder.dateRangePicker()
        val now = Calendar.getInstance()
        pickerBuilder.setSelection(Pair(now.timeInMillis, now.timeInMillis))
        pickerBuilder.setTheme(R.style.DateRangePickerCustomTheme)

        picker = pickerBuilder.build()
        picker.addOnNegativeButtonClickListener { picker.dismiss() }
        picker.addOnPositiveButtonClickListener {
            // setSelectedDates(it.first, it.second)
        }
    }

    @Composable
    fun PrintFoodDiaryPage() {
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
                        .clickable {  },
                    text = printFoodDiaryViewModel.fromDateSelected.toString(),
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
                        .clickable {  },
                    text = printFoodDiaryViewModel.toDateSelected.toString(),
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                enabled = false,
                onClick = {},
                shape = RoundedCornerShape(45.dp),
            ) {
                Text("Generate PDF", fontSize = 20.sp)
            }
        }
    }

    companion object {

    }
}