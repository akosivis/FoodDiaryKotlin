package com.viselvis.fooddiarykotlin.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.application.FoodItemListApplication
import com.viselvis.fooddiarykotlin.databinding.FragmentFoodHistoryBinding
import com.viselvis.fooddiarykotlin.ui.theme.NoteEatTheme
import com.viselvis.fooddiarykotlin.viewmodels.FoodHistoryViewModel
import com.viselvis.fooddiarykotlin.viewmodels.FoodHistoryViewModelFactory

class FoodHistoryFragment : Fragment() {

    private var binding: FragmentFoodHistoryBinding? = null
    private val foodHistoryViewModel: FoodHistoryViewModel by viewModels {
        FoodHistoryViewModelFactory((context?.applicationContext as FoodItemListApplication).repository)
    }

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

    @Composable
    fun FoodHistoryPage() {
        val uiState by foodHistoryViewModel.uiState.collectAsState()

        NoteEatTheme {
            Surface (modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(8.dp)
                                    .clickable {
                                        foodHistoryViewModel.getPreviousDay()
                                    },
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = "Previous day"
                            )
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = foodHistoryViewModel.dateToDisplay,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = foodHistoryViewModel.dayToDisplay,
                                    fontSize = 16.sp
                                )
                            }
                            if (uiState.givenCalendarInstance != foodHistoryViewModel.dateToday) {
                                Icon(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(8.dp)
                                        .clickable {
                                            foodHistoryViewModel.getNextDay()
                                        },
                                    imageVector = Icons.Rounded.ArrowForward,
                                    contentDescription = "Next day"
                                )
                            } else {
//                    Box(
//                        modifier =
//                    )
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    if (uiState.foodItems.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            items(uiState.foodItems) { foodItem ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                    ) {
                                        Text(
                                            text = foodHistoryViewModel.getTimeFromLong(foodItem.foodItemCreated.time)
                                                .toString(),
                                            fontSize = 16.sp
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = foodItem.foodItemTitle,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp
                                            )
                                            Text(text = foodItem.foodItemDetails)
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = "No food items recorded",
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {

    }
}
