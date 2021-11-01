package com.example.skgym.Room.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.LiveData
import com.example.skgym.Room.Dao.CartDao
import com.example.skgym.data.Cart
import com.example.skgym.mvvm.repository.BaseRepository

class CartDatabaseRepository(private val cartDao: CartDao, var context: Context) :
    BaseRepository(context) {
    val readUnpaidCartData: LiveData<List<Cart>> = cartDao.getUnpaidCart()
    val readAllData: LiveData<List<Cart>> = cartDao.getCart()
    private val ref = fDatabase.reference.child("Orders")
    private val prefs = context.getSharedPreferences("Prefs", MODE_PRIVATE)


    suspend fun addProductToCart(product: Cart) = cartDao.insertProduct(product)

    fun setPaymentToTrue(cart: Cart) = cartDao.updateProduct(cart)

    fun pushOrdersDb(cart: Cart) =
        ref.push().setValue(
            hashMapOf(
                "Product" to cart.product,
                "Quantity" to cart.quantity,
                "Customer" to prefs.getString("Name", "Null")
            )
        )
}