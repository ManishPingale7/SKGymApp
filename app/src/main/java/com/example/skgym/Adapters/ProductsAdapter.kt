package com.example.skgym.Adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.skgym.data.Product
import com.example.skgym.databinding.ProductlistitemBinding

class ProductsAdapter() :
    ListAdapter<Product, ProductsAdapter.viewHolder>(DiffCallBack()) {

    private lateinit var mListener: onItemClickedListener

    private lateinit var context: Context


    interface onItemClickedListener {
        fun onItemClicked(product: Product)
    }

    fun setOnItemClickListener(onItemClickedListener: onItemClickedListener) {
        mListener = onItemClickedListener
    }

    fun setContext(context2: Context) {
        context = context2
    }

    inner class viewHolder(private val binding: ProductlistitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            Log.d("TAG", "bind: BINDING THIS -$product")
            val text = "₹ ${product.price}"
            binding.apply {
                productNameCard.text = product.name
                productPrice.text = text
                Glide.with(context)
                    .load(product.productImage)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.productInfoLay.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressBarProduct.visibility = View.GONE
                            binding.productInfoLay.visibility = View.VISIBLE
                            return false
                        }

                    })
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