package com.viselvis.fooddiarykotlin.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.viselvis.fooddiarykotlin.viewmodels.MainViewModel
import com.viselvis.fooddiarykotlin.viewmodels.MainViewModelFactory

class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((activity?.application as FoodItemListApplication).repository)
    }

    private val adapter = FoodItemAdapter(0)
    private lateinit var listener : MainFragmentListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_main, container, false)

        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false)

        binding.lltAddFoodItem.setOnClickListener {
            // call an activity here
//            val intentToAddFoodItem = Intent(activity, AddFoodItemActivity::class.java)
//            startActivity(intentToAddFoodItem)
            listener.navigateToSelectFoodTypes()
        }

        binding.cdvViewHistory.setOnClickListener {
            val intentToFoodHistory = Intent(activity, FoodHistoryActivity::class.java)
            startActivity(intentToFoodHistory)
        }

        // declare adapter here
        binding.rcvFoodHistory.adapter = adapter
        binding.rcvFoodHistory.layoutManager = LinearLayoutManager(activity)

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {
            is MainFragmentListener -> listener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.allFoodItems.observe(
                viewLifecycleOwner, Observer {
                    foodItems -> foodItems.let { adapter.submitList(it) }
        })
    }

    interface MainFragmentListener {
        fun navigateToSelectFoodTypes()
    }

    companion object {

    }
}