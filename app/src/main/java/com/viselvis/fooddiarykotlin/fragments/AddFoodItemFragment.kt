package com.viselvis.fooddiarykotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.application.FoodItemListApplication
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.databinding.FragmentAddFoodItemBinding
import com.viselvis.fooddiarykotlin.utils.BaseChip
import com.viselvis.fooddiarykotlin.utils.BaseTextField
import com.viselvis.fooddiarykotlin.viewmodels.AddFoodItemViewModel
import com.viselvis.fooddiarykotlin.viewmodels.AddFoodItemViewModelFactory
import java.util.*

class AddFoodItemFragment : Fragment() {

    private var binding: FragmentAddFoodItemBinding? = null
    private val addFoodItemViewModel: AddFoodItemViewModel by viewModels {
        AddFoodItemViewModelFactory((context?.applicationContext as FoodItemListApplication).repository)
    }
    private var foodItemType by mutableStateOf(-1)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_food_item, container, false)
        foodItemType = arguments?.getInt("foodTypeId")!!
        addFoodItemViewModel.isDataInserted.observe(viewLifecycleOwner) { isSuccess ->
            if (!isSuccess.equals(-1)) {
                // go back to the allnotes fragment
                Toast.makeText(activity, "Insertion is successful!", Toast.LENGTH_LONG).show()
                activity?.supportFragmentManager?.popBackStack()
            } else {
                Toast.makeText(activity, "Insertion is not successful", Toast.LENGTH_LONG).show()
            }
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.composeView?.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AddFoodItemPage()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    @Preview
    fun AddFoodItemPage() {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            BaseTextField(
                text = addFoodItemViewModel.itemName,
                onTextChanged = { addFoodItemViewModel.itemName = it },
                placeholderText = if (foodItemType == 1) {
                    "Medicine item name"
                } else {
                    "Food item name"
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            BaseTextField(
                text = addFoodItemViewModel.itemDetail,
                onTextChanged = { addFoodItemViewModel.itemDetail = it },
                placeholderText = if (foodItemType == 1) {
                    "Medicine item details"
                } else {
                    "Food item details"
                }
            )

            Spacer(modifier = Modifier.height(10.dp))
            when (foodItemType) {
                0 -> {
                    Text("Contains: ")
                    Spacer(modifier = Modifier.height(5.dp))
                    Box (
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        com.viselvis.fooddiarykotlin.utils.FlowRow(
                            horizontalGap = 8.dp,
                            verticalGap = 8.dp,
                        ) {
                            for (ingredient in addFoodItemViewModel.itemIngredientsList) {
                                BaseChip (
                                    text = ingredient,
                                    clickable = {}
                                )
                            }

                            BaseTextField(
                                givenModifier = Modifier.fillMaxWidth().onKeyEvent { event ->
                                    if (event.key == Key.Enter) {
                                        val input = addFoodItemViewModel.itemIngredientInput
                                        if (input.isNotEmpty()) {
                                            addFoodItemViewModel.insertIngredient(input)
                                        }
                                        true
                                    }
                                    false
                                },
                                text = addFoodItemViewModel.itemIngredientInput,
                                onTextChanged = {
                                    addFoodItemViewModel.itemIngredientInput = it },
                                placeholderText = stringResource(R.string.ingredients_hint)
                            )
                        }
                    }
                }
                else -> {}
            }
            
            Button (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                shape = RoundedCornerShape(45.dp),
                onClick = {
                    insertFoodItemOnDb()
                }
            ) {
                Text (
                    text = if (foodItemType == 1) {
                        "Add medicine intake"
                    } else {
                        "Add food item"
                    }
                )
            }
        }
    }

    private fun insertFoodItemOnDb() {
        val dateCreated = Calendar.getInstance().time
        val dateModified = Calendar.getInstance().time
        val newFoodItem = FoodItemModel(
            foodItemType = foodItemType,
            foodItemTitle = addFoodItemViewModel.itemName,
            foodItemDetails = addFoodItemViewModel.itemDetail,
            foodItemCreated = dateCreated,
            foodItemLastModified = dateModified,
            foodItemIngredients = addFoodItemViewModel.itemIngredientsList
        )

        addFoodItemViewModel.saveFoodItem(newFoodItem)
    }

    companion object {

    }
}