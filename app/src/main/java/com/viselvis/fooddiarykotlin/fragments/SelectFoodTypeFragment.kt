package com.viselvis.fooddiarykotlin.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.adapter.FoodTypeAdapter
import com.viselvis.fooddiarykotlin.database.FoodTypeModel
import com.viselvis.fooddiarykotlin.databinding.FragmentSelectFoodTypeBinding

class SelectFoodTypeFragment : Fragment() {

    private lateinit var binding: FragmentSelectFoodTypeBinding
    private lateinit var adapter: FoodTypeAdapter
    private val initialList = listOf (
        FoodTypeModel(0,"Add food item", ""),
        FoodTypeModel(1, "Add medicine taken", "")
    )

    private lateinit var listener : SelectFoodTypeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_food_type, container, false)
        loadView()
        return binding.root
    }

    private fun loadView() {
        adapter = FoodTypeAdapter(
            object : SelectFoodTypeListener {
                override fun navigateToAddFoodItemFragment() {
                    listener.navigateToAddFoodItemFragment()
                }
            }
        )
        binding.rcvFoodTypes.adapter = adapter
        adapter.submitList(initialList)
        binding.rcvFoodTypes.layoutManager = GridLayoutManager(activity, 3)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {
            is SelectFoodTypeListener -> listener = context
        }
    }

    interface SelectFoodTypeListener {
        fun navigateToAddFoodItemFragment()
    }

    companion object {

    }
}