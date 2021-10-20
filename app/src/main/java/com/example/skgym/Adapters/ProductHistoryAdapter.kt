package com.example.skgym.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.skgym.data.Cart
import com.example.skgym.data.Product
import com.example.skgym.databinding.CartitemHistoryBinding
import com.google.gson.Gson

class ProductHistoryAdapter(val context: Context) :
    ListAdapter<Cart, ProductHistoryAdapter.CartViewHolder>(DiffCallBack()) {
    private val gson = Gson()

    inner class CartViewHolder(val binding: CartitemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cart: Cart) {
            binding.apply {
                val product = gson.fromJson(cart.product, Product::class.java)
                val text = "₹ ${product.price}"
                productNameCard.text = product.name
                productPrice.text = text
                productQuantity.text = cart.quantity.toString()
                Glide.with(context)
                    .load(product.productImage)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .fitCenter()
                    .into(productImage)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            CartitemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallBack : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart) = oldItem == newItem
    }

}