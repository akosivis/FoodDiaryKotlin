package com.viselvis.fooddiarykotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.viselvis.fooddiarykotlin.database.FoodItemModel
import com.viselvis.fooddiarykotlin.R
import java.text.SimpleDateFormat
import java.util.*

class FoodItemAdapter(private val viewIndicator: Int) : ListAdapter<FoodItemModel, FoodItemAdapter.FoodItemViewHolder>(FoodItemComparator()) {

    class FoodItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind (viewIndicator: Int, title: String?, body: String?, foodItemCreated: Long, isSpecialItem: Int) {
            /*
                viewIndicator values:

                0 - from MainFragment
                1 - from FoodHistoryActivity

               specialItem

               0 - first item
               1 - last item
               2 - only item
             */
            val foodItemViewTitle: TextView = itemView.findViewById(R.id.tv_title)
            val foodItemViewBody: TextView = itemView.findViewById(R.id.tv_body)
            val connectorLayout: ConstraintLayout = itemView.findViewById(R.id.clt_connectorLayout)
            val connectorUp: ImageView = itemView.findViewById(R.id.iv_connectorUp)
            val connectorDown: ImageView = itemView.findViewById(R.id.iv_connectorDown)
            val timeLayout: RelativeLayout = itemView.findViewById(R.id.rlt_dateLayout)
            val timeView: TextView = itemView.findViewById(R.id.tv_dateCreated)

            if (viewIndicator == 1) {
                timeView.text = getTimeFromLong(foodItemCreated)

                connectorUp.visibility = View.VISIBLE
                connectorDown.visibility = View.VISIBLE
                when(isSpecialItem) {
                    0 -> connectorUp.visibility = View.INVISIBLE
                    1 -> connectorDown.visibility = View.INVISIBLE
                    2 -> {
                        connectorUp.visibility = View.INVISIBLE
                        connectorDown.visibility = View.INVISIBLE
                    }
                }

                timeLayout.visibility = View.VISIBLE
                connectorLayout.visibility = View.VISIBLE

                itemView.setOnClickListener {

                }
            }

            foodItemViewTitle.text = title
            foodItemViewBody.text = body
        }

        private fun getTimeFromLong(foodItemCreated: Long): CharSequence? {
            val date = Date(foodItemCreated)
            val format = SimpleDateFormat("HH:mm")
            return format.format(date)
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
        var specialItem = -1

        if (position == 0) {
            if (itemCount == 1) {
                specialItem = 2
            } else {
                specialItem = 0
            }
        } else if (position == itemCount - 1) {
            specialItem = 1
        }

        holder.bind(viewIndicator, current.foodItemTitle, current.foodItemDetails, current.foodItemCreated.time, specialItem)
    }

}


