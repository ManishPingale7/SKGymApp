package com.example.skgym.Adapters

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skgym.data.Plan
import com.example.skgym.databinding.PlanlistitemBinding

class ViewPlansAdapter : ListAdapter<Plan, ViewPlansAdapter.PlansViewHolder>(DiffCallBack()) {
    private lateinit var mListener: onItemClickedListener

    interface onItemClickedListener {
        fun onItemClicked(position: Int)
    }

    fun setOnItemClickListener(onItemClickedListener: onItemClickedListener) {
        mListener = onItemClickedListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlansViewHolder {
        return PlansViewHolder(
            PlanlistitemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PlansViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlansViewHolder(
        val binding: PlanlistitemBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        var text=""
        fun bind(plan: Plan) {
            Log.d(TAG, "bind: ${plan.pt!!}")
            text = if (plan.pt) {
                "PT"
            } else {
                "Normal"
            }
            binding.apply {
                cardPlanName.text = plan.name
              //  cardPlanDesc.text = plan.desc
                cardDuration.text = plan.timeNumber
                isPersonal.text = text
                cardFees.text = plan.fees

            }
            binding.apply {
                plansLayout.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        mListener.onItemClicked(adapterPosition)
                    }
                }
            }
        }
    }

}

class DiffCallBack : DiffUtil.ItemCallback<Plan>() {
    override fun areItemsTheSame(oldItem: Plan, newItem: Plan) =
        oldItem.name == newItem.name


    override fun areContentsTheSame(oldItem: Plan, newItem: Plan) =
        oldItem == newItem

}
