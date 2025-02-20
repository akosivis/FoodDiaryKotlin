package com.viselvis.fooddiarykotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.application.FoodItemListApplication
import com.viselvis.fooddiarykotlin.databinding.FragmentAddFoodItemBinding
import com.viselvis.fooddiarykotlin.ui.theme.NoteEatTheme
import com.viselvis.fooddiarykotlin.utils.BaseChip
import com.viselvis.fooddiarykotlin.utils.BaseTextField
import com.viselvis.fooddiarykotlin.viewmodels.AddFoodItemViewModel
import com.viselvis.fooddiarykotlin.viewmodels.AddFoodItemViewModelFactory

class AddFoodItemFragment : Fragment() {

    private var binding: FragmentAddFoodItemBinding? = null
    private val addFoodItemViewModel: AddFoodItemViewModel by viewModels {
        AddFoodItemViewModelFactory((context?.applicationContext as FoodItemListApplication).foodItemsRepo)
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
                Toast.makeText(activity, resources.getString(R.string.insert_success), Toast.LENGTH_LONG).show()
                activity?.supportFragmentManager?.popBackStack()
            } else {
                Toast.makeText(activity, resources.getString(R.string.insert_not_success), Toast.LENGTH_LONG).show()
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
        NoteEatTheme {
            Surface (modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    BaseTextField(
                        text = addFoodItemViewModel.itemName,
                        onTextChanged = {
                            addFoodItemViewModel.itemName = it
                            if (addFoodItemViewModel.itemName.isNotEmpty()) {
                                addFoodItemViewModel.errorMessage = ""
                            }
                        },
                        placeholderText = if (foodItemType == 1) {
                            stringResource(id = R.string.meds_item_name)
                        } else {
                            stringResource(id = R.string.food_item_name_not_italic)
                        },
                    )

                    if (addFoodItemViewModel.errorMessage.isNotEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = addFoodItemViewModel.errorMessage,
                                fontSize = 14.sp,
                                fontStyle = FontStyle.Normal,
                                color = Color.Red,
                                textAlign = TextAlign.Start
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    BaseTextField(
                        text = addFoodItemViewModel.itemDetail,
                        onTextChanged = { addFoodItemViewModel.itemDetail = it },
                        placeholderText = if (foodItemType == 1) {
                            stringResource(id = R.string.meds_item_details)
                        } else {
                            stringResource(id = R.string.food_item_details)
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    when (foodItemType) {
                        0 -> {
                            Text("Contains: ")
                            Spacer(modifier = Modifier.height(5.dp))
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                com.viselvis.fooddiarykotlin.utils.FlowRow(
                                    horizontalGap = 8.dp,
                                    verticalGap = 8.dp,
                                ) {
                                    for (ingredient in addFoodItemViewModel.itemIngredientsList) {
                                        BaseChip(
                                            text = ingredient
                                        )
                                    }

                                    BaseTextField(
                                        givenModifier = Modifier
                                            .fillMaxWidth()
                                            .onKeyEvent { event ->
                                                if (event.key == Key.Enter) {
                                                    val input =
                                                        addFoodItemViewModel.itemIngredientInput
                                                    if (input.isNotEmpty()) {
                                                        addFoodItemViewModel.insertIngredient(input)
                                                    }
                                                    true
                                                }
                                                false
                                            },
                                        text = addFoodItemViewModel.itemIngredientInput,
                                        onTextChanged = {
                                            addFoodItemViewModel.itemIngredientInput = it
                                        },
                                        placeholderText = stringResource(R.string.ingredients_hint),
                                        isSingleLine = true
                                    )
                                }
                            }
                        }
                        else -> {}
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        shape = RoundedCornerShape(45.dp),
                        onClick = {
                            addFoodItemViewModel.insertFoodItemOnDb(foodItemType)
                        }
                    ) {
                        Text(
                            text = if (foodItemType == 1) {
                                stringResource(id = R.string.add_meds_intake)
                            } else {
                                stringResource(id = R.string.add_food_item)
                            }
                        )
                    }
                }
            }
        }
    }

    companion object {

    }
}