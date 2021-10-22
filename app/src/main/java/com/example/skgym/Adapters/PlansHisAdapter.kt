package com.example.skgym.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skgym.data.Plan
import com.example.skgym.data.PlansDB
import com.example.skgym.databinding.PlanlistitemhisBinding
import com.google.gson.Gson

class PlansHisAdapter : ListAdapter<PlansDB, PlansHisAdapter.PlansViewHolder>(DifCallBack()) {
    val gson = Gson()

    inner class PlansViewHolder(val binding: PlanlistitemhisBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var text = ""
        var RESULT = View.VISIBLE
        fun bind(item: PlansDB?) {
            val plan = gson.fromJson(item!!.plan, Plan::class.java)
            if (plan.pt!!) {
                text = "PT"
                RESULT = View.VISIBLE

            } else {
                text = "Normal"
                RESULT = View.INVISIBLE
            }
            binding.apply {
                cardPlanName.text = plan.name
                //  cardPlanDesc.text = plan.desc
                cardDuration.text = plan.timeNumber
                isPersonal.text = text
                cardFees.text = plan.fees
                badgeGold.visibility = RESULT
                boughtOn.text = item.date
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlansViewHolder {
        return PlansViewHolder(
            PlanlistitemhisBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PlansViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DifCallBack : DiffUtil.ItemCallback<PlansDB>() {
    override fun areItemsTheSame(oldItem: PlansDB, newItem: PlansDB) = oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: PlansDB, newItem: PlansDB) = oldItem == newItem

}
