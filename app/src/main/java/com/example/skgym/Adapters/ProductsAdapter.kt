package com.example.skgym.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.skgym.data.Product
import com.example.skgym.databinding.ProductlistitemBinding

class ProductsAdapter(val context: Context) :
    ListAdapter<Product, ProductsAdapter.viewHolder>(DiffCallBack()) {

    private lateinit var mListener: onItemClickedListener

    interface onItemClickedListener {
        fun onItemClicked(product: Product)
    }

    fun setOnItemClickListener(onItemClickedListener: onItemClickedListener) {
        mListener = onItemClickedListener
    }

    inner class viewHolder(private val binding: ProductlistitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            Log.d("TAG", "bind: BINDING THIS -$product")
            val text="₹ ${product.price}"
            binding.apply {
                productNameCard.text = product.name
                productPrice.text = text
                Glide.with(context)
                    .load(product.productImage)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .fitCenter()
                    .into(productImage)
                addButton.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        mListener.onItemClicked(getItem(adapterPosition))
                    }
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(
            ProductlistitemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallBack : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) =
            oldItem.key == newItem.key

        override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem

    }
}