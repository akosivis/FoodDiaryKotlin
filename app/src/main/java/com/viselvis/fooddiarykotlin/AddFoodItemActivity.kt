package com.viselvis.fooddiarykotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.viselvis.fooddiarykotlin.application.FoodItemListApplication
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.databinding.ActivityAddFoodItemBinding
import com.viselvis.fooddiarykotlin.fragments.MainFragment
import com.viselvis.fooddiarykotlin.viewmodels.AddFoodItemViewModel
import com.viselvis.fooddiarykotlin.viewmodels.AddFoodItemViewModelFactory
import java.util.*

class AddFoodItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddFoodItemBinding
    private val addFoodItemViewModel: AddFoodItemViewModel by viewModels {
        AddFoodItemViewModelFactory((application as FoodItemListApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddFoodItemBinding.inflate(layoutInflater)
        val view = binding.root

        addFoodItemViewModel.isDataInserted.observe(this, Observer { isSuccess ->
            if (!isSuccess.equals(-1)) {
                // go back to the allnotes fragment
                Toast.makeText(this, "Insertion is successful!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Insertion is successful",  Toast.LENGTH_LONG).show()
            }
        })

        binding.mbtnSaveItem.setOnClickListener {
            if (!TextUtils.isEmpty(binding.edtFoodItemName.text) && !TextUtils.isEmpty(binding.edtFoodItemDetails.text) ) {
                val foodItemName = binding.edtFoodItemName.text.toString()
                val foodItemDetails = binding.edtFoodItemDetails.text.toString()

                insertFoodItemOnDb(foodItemName, foodItemDetails)
            } else {
                Toast.makeText(this, "One/both of the fields are still empty!", Toast.LENGTH_LONG).show()
            }
        }

        setContentView(view)
    }

    private fun insertFoodItemOnDb(name: String, details: String) {
        val dateCreated = Calendar.getInstance().timeInMillis
        val dateModified = Calendar.getInstance().timeInMillis
        val newFoodItem = FoodItemModel(name, details, dateCreated, dateModified)

        addFoodItemViewModel.saveFoodItem(newFoodItem)
    }
}