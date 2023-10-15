package com.viselvis.fooddiarykotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.database.FoodTypeModel

class FoodTypeAdapter() :
    ListAdapter<FoodTypeModel, FoodTypeAdapter.FoodTypeViewHolder>(FoodTypeComparator()) {

    class FoodTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(foodTypeTitle: String, foodTypeIcon: String) {

        }

        companion object {
            fun create(parent: ViewGroup): FoodTypeViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.food_type_item, parent, false)
                return FoodTypeViewHolder(view)
            }
        }
    }

    class FoodTypeComparator : DiffUtil.ItemCallback<FoodTypeModel>(){
        override fun areItemsTheSame(oldItem: FoodTypeModel, newItem: FoodTypeModel): Boolean {
            return oldItem === newItem
        }


        override fun areContentsTheSame(oldItem: FoodTypeModel, newItem: FoodTypeModel): Boolean {
            TODO("Not yet implemented")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodTypeViewHolder {
        return FoodTypeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FoodTypeViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.foodTypeTitle, current.foodTypeIcon)
    }

//    fun updateData(newData: List<FoodTypeModel>){
//        this.submitList(newData) //submit the data again
//    }
}