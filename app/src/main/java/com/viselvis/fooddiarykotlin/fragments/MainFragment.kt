package com.viselvis.fooddiarykotlin.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.viselvis.fooddiarykotlin.activity.AddFoodItemActivity
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.activity.FoodHistoryActivity
import com.viselvis.fooddiarykotlin.adapter.FoodItemAdapter
import com.viselvis.fooddiarykotlin.application.FoodItemListApplication
import com.viselvis.fooddiarykotlin.databinding.FragmentMainBinding
import com.viselvis.fooddiarykotlin.utils.BaseClickableCard
import com.viselvis.fooddiarykotlin.viewmodels.MainViewModel
import com.viselvis.fooddiarykotlin.viewmodels.MainViewModelFactory

class MainFragment : Fragment() {

    private var binding: FragmentMainBinding? = null
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((activity?.application as FoodItemListApplication).repository)
    }

    private val adapter = FoodItemAdapter(0)
    private lateinit var listener : MainFragmentListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {
            is MainFragmentListener -> listener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.composeView?.apply {
            setContent {
                MainFragmentPage()
            }
        }
    }

    interface MainFragmentListener {
        fun navigateToSelectFoodTypes()
        fun navigateToFoodHistory()
    }

    @Composable
    fun MainFragmentPage(){
        val recentFoodItems by mainViewModel.allFoodItems.observeAsState()

        Column (modifier = Modifier.padding(15.dp)) {
            Text("Hi User!")

            Text(text = "What have you eaten today?")

            // LazyColumn
//            LazyColumn(modifier = Modifier.fillMaxWidth()) {
//                items(recentFoodItems) { item ->
//                    Card (
//                        modifier = Modifier.fillMaxWidth(),
//                    ) {
//                        Row (
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(8.dp)
//                        ) {
//                            Column {
//                                Text(
//                                    text = item.foodItemTitle,
//                                    fontWeight = FontWeight.Bold,
//                                    fontSize = 16.sp
//                                )
//                                Text(text = item.foodItemDetails)
//                            }
//                        }
//                    }
//                }
//            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BaseClickableCard(clickable = {
                    listener.navigateToSelectFoodTypes()
                }, name = "Add food item")
                BaseClickableCard(clickable = {
                    listener.navigateToFoodHistory()
                }, name = "View food history")
            }
        }
    }

    companion object {

    }
}