package com.viselvis.fooddiarykotlin.activity

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.viselvis.fooddiarykotlin.adapter.FoodItemAdapter
import com.viselvis.fooddiarykotlin.application.FoodItemListApplication
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.databinding.ActivityFoodHistoryBinding
import com.viselvis.fooddiarykotlin.viewmodels.FoodHistoryViewModel
import com.viselvis.fooddiarykotlin.viewmodels.FoodHistoryViewModelFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class FoodHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodHistoryBinding
    private lateinit var daySelected: Calendar
    private val adapter = FoodItemAdapter()
    private lateinit var dateToday: Calendar
    private lateinit var dateSelected: Date

    private val foodHistoryViewModel: FoodHistoryViewModel by viewModels {
        FoodHistoryViewModelFactory((application as FoodItemListApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFoodHistoryBinding.inflate(layoutInflater)
        val view = binding.root
        binding.ivBackPress.setOnClickListener {
            onBackPressed()
        }
        binding.rltPrevDate.setOnClickListener {
            daySelected.add(Calendar.DAY_OF_YEAR, -1)
            getDayDetails()
            binding.rltNextDate.isEnabled = true
            binding.rltNextDate.visibility = View.VISIBLE
        }
        binding.rltNextDate.setOnClickListener {
            daySelected.add(Calendar.DAY_OF_YEAR, 1)
            getDayDetails()
            if (daySelected == dateToday) {
                binding.rltNextDate.visibility = View.INVISIBLE
                binding.rltNextDate.isEnabled = false
            }
        }

        binding.rltNextDate.visibility = View.INVISIBLE
        binding.rltNextDate.isEnabled = false
        binding.rcvFoodHistoryByDay.adapter = adapter
        binding.rcvFoodHistoryByDay.layoutManager = LinearLayoutManager(this)

        daySelected = Calendar.getInstance()
        dateToday = Calendar.getInstance()
        getDayDetails()

        setContentView(view)
    }

    private fun getDayDetails() {
        binding.tvCurrentDate.text = getCurrentDate()
        binding.tvCurrentDay.text = getCurrentDay()

        getFoodItemsByDate()
    }

    private fun getFoodItemsByDate() {
        foodHistoryViewModel.allFoodItems.observe(this, Observer { foodItems ->
            foodItems.let { adapter.submitList(foodItemsListByDate(it, daySelected)) }
        })
    }

    private fun foodItemsListByDate(list: List<FoodItemModel>, date: Calendar) : List<FoodItemModel> {
        val toReturnList = mutableListOf<FoodItemModel>()
        for (model in list) {
            val dateObject = Date(model.foodItemCreated)
            val cal: Calendar = Calendar.getInstance()
            cal.time = dateObject

            if (compareDate(cal, date)) { // if foodItemCreated is date today
                toReturnList.add(model)
            }
        }

        return toReturnList
    }

    private fun compareDate(dateObject: Calendar, date: Calendar): Boolean {
        val dateFoodItemDay = dateObject.get(Calendar.DAY_OF_MONTH)
        val dateFoodItemMonth = dateObject.get(Calendar.MONTH)
        val dateFoodItemYear = dateObject.get(Calendar.YEAR)
        val dateSelectedDay = date.get(Calendar.DAY_OF_MONTH)
        val dateSelectedMonth = date.get(Calendar.MONTH)
        val dateSelectedYear = date.get(Calendar.YEAR)

        if((dateFoodItemDay == dateSelectedDay) &&
            (dateFoodItemMonth == dateSelectedMonth) && (dateFoodItemYear == dateSelectedYear) ) {
            return true
        }
        return false;
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDay(): CharSequence? {
        val sdf = SimpleDateFormat("EEEE")
        var date = daySelected.time
        return sdf.format(date)
    }

    private fun getCurrentDate(): CharSequence? {
        var date = daySelected.time
        dateSelected = daySelected.time
        return DateFormat.getDateInstance().format(date)
    }
}