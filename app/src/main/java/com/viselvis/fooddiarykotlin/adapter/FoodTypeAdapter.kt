package com.viselvis.fooddiarykotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.database.FoodTypeModel
import com.viselvis.fooddiarykotlin.fragments.SelectFoodTypeFragment
import kotlinx.android.synthetic.main.food_type_item.view.*

class FoodTypeAdapter(private val listener: SelectFoodTypeFragment.SelectFoodTypeListener) :
    ListAdapter<FoodTypeModel, FoodTypeAdapter.FoodTypeViewHolder>(FoodTypeComparator()) {

    class FoodTypeViewHolder(
        itemView: View,
        private val listener: SelectFoodTypeFragment.SelectFoodTypeListener
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(foodTypeItem: FoodTypeModel?) {
            itemView.tv_foodTypeName.text = foodTypeItem?.foodTypeTitle
            itemView.llt_card.setOnClickListener {
                when (foodTypeItem?.foodTypeId) {
                    1 -> {}
                    else -> {
                        listener.navigateToAddFoodItemFragment()
                    }
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup, listener: SelectFoodTypeFragment.SelectFoodTypeListener): FoodTypeViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.food_type_item, parent, false)
                return FoodTypeViewHolder(view, listener)
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
        return FoodTypeViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: FoodTypeViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

//    fun updateData(newData: List<FoodTypeModel>){
//        this.submitList(newData) //submit the data again
//    }
}