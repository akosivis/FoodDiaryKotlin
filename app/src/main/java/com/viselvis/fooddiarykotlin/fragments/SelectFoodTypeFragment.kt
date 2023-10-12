package com.viselvis.fooddiarykotlin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.databinding.FragmentSelectFoodTypeBinding

class SelectFoodTypeFragment : Fragment() {

    private lateinit var binding: FragmentSelectFoodTypeBinding

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
        binding.rcvFoodTypes.adapter = adapter
        binding.rcvFoodTypes.layoutManager = GridLayoutManager(activity, 3)
    }

    companion object {

    }
}