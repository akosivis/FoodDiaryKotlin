package com.viselvis.fooddiarykotlin.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.adapter.FoodItemAdapter
import com.viselvis.fooddiarykotlin.application.FoodItemListApplication
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.databinding.FragmentAddFoodItemBinding
import com.viselvis.fooddiarykotlin.databinding.FragmentFoodHistoryBinding
import com.viselvis.fooddiarykotlin.viewmodels.AddFoodItemViewModel
import com.viselvis.fooddiarykotlin.viewmodels.AddFoodItemViewModelFactory
import com.viselvis.fooddiarykotlin.viewmodels.FoodHistoryViewModel
import com.viselvis.fooddiarykotlin.viewmodels.FoodHistoryViewModelFactory
import java.text.DateFormat
import java.util.*

class FoodHistoryFragment : Fragment() {

    private var binding: FragmentFoodHistoryBinding? = null
    private val foodHistoryViewModel: FoodHistoryViewModel by viewModels {
        FoodHistoryViewModelFactory((context?.applicationContext as FoodItemListApplication).repository)
    }
    private var daySelected: Calendar = Calendar.getInstance()
    private var dateToday: Calendar = Calendar.getInstance()
    // private lateinit var dateSelected: Date

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_food_history, container, false)
        // Inflate the layout for this fragment
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.composeView?.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                FoodHistoryPage()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        when (context) {
//            is SelectFoodTypeFragment.SelectFoodTypeListener -> listener = context
//        }

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


    @Composable
    fun FoodHistoryPage() {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Row {
                Icon(
                    modifier = Modifier.padding(8.dp).clickable { },
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Previous day"
                )
                Column (horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Date here")
                    Text("Day here")
                }
                if (daySelected != dateToday) {
                    Icon(
                        modifier = Modifier.padding(8.dp).clickable { },
                        imageVector = Icons.Rounded.ArrowForward,
                        contentDescription = "Next day"
                    )
                } else {
//                    Box(
//                        modifier =
//                    )
                }

            }

            if (foodHistoryViewModel.currentFoodItemListState.foodItems.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(foodHistoryViewModel.currentFoodItemListState.foodItems) { foodItem ->
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(foodItem.foodItemCreated.time.toString())
                            Column {
                                Text(text = foodItem.foodItemTitle)
                                Text(text = foodItem.foodItemDetails)
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(modifier = Modifier.align(Alignment.Center), text = "No food items recorded")
                }
            }
        }
    }

    companion object {

    }
}
