package com.viselvis.fooddiarykotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.R

class FoodItemAdapter : ListAdapter<FoodItemModel, FoodItemAdapter.FoodItemViewHolder>(FoodItemComparator()){
    class FoodItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val foodItemViewTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val foodItemViewBody: TextView = itemView.findViewById(R.id.tv_body)

        fun bind (title: String?, body: String?) {
            foodItemViewTitle.text = title
            foodItemViewBody.text = body
        }

        companion object {
            fun create(parent: ViewGroup): FoodItemViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.food_list_item, parent, false)
                return FoodItemViewHolder(view)
            }
        }
    }

    class FoodItemComparator : DiffUtil.ItemCallback<FoodItemModel>(){
        override fun areItemsTheSame(oldItem: FoodItemModel, newItem: FoodItemModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: FoodItemModel, newItem: FoodItemModel): Boolean {
            return oldItem.foodItemId == newItem.foodItemId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        return FoodItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val current = getItem(position)

        holder.bind(current.foodItemTitle, current.foodItemDetails)
    }
}


