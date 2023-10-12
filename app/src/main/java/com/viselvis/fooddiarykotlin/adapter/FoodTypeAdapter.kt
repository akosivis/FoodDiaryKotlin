package com.viselvis.fooddiarykotlin.adapter

import android.view.View
import android.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.viselvis.fooddiarykotlin.database.FoodItemModel

class FoodTypeAdapter(private val viewIndicator: Int) : ListAdapter<FoodItemModel, FoodTypeAdapter.FoodTypeViewHolder> {

    class FoodTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {

        }
    }
}