package com.viselvis.fooddiarykotlin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.application.FoodItemListApplication
import com.viselvis.fooddiarykotlin.databinding.FragmentAddFoodItemBinding
import com.viselvis.fooddiarykotlin.viewmodels.AddFoodItemViewModel
import com.viselvis.fooddiarykotlin.viewmodels.AddFoodItemViewModelFactory

class AddFoodItemFragment : Fragment() {

    private var binding: FragmentAddFoodItemBinding? = null
    private val addFoodItemViewModel: AddFoodItemViewModel by viewModels {
        AddFoodItemViewModelFactory((context?.applicationContext as FoodItemListApplication).repository)
    }
    private var foodItemType by mutableStateOf(-1)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_food_item, container, false)
        foodItemType = arguments?.getInt("foodTypeId")!!
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.composeView?.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Column (
                    modifier = Modifier.fillMaxSize()
                ) {
                    TextField(
                        value = addFoodItemViewModel.itemName,
                        onValueChange = {
                            addFoodItemViewModel.itemName = it
                        },
                        placeholder = {
                            Text(
                                if (foodItemType == 1) {
                                    "Medicine item name"
                                } else {
                                    "Food item name"
                                }
                            )
                        }
                    )

                    TextField(
                        value = addFoodItemViewModel.itemDetail,
                        onValueChange = {
                            addFoodItemViewModel.itemDetail = it
                        },
                        placeholder = {
                            Text(
                                if (foodItemType == 1) {
                                    "Medicine item details"
                                } else {
                                    "Food item details"
                                }
                            )
                        }
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {

    }
}