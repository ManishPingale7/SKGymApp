package com.example.skgym.Room.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.example.skgym.Room.Dao.CartDao
import com.example.skgym.data.Cart
import com.example.skgym.mvvm.repository.BaseRepository
import com.example.skgym.utils.OrderStatus

class CartDatabaseRepository(private val cartDao: CartDao, var context: Context) :
    BaseRepository(context) {
    val readUnpaidCartData: LiveData<List<Cart>> = cartDao.getUnpaidCart()
    val readAllData: LiveData<List<Cart>> = cartDao.getCart()
    private val ref = fDatabase.reference.child("Orders")
    private val prefs = context.getSharedPreferences("Prefs", MODE_PRIVATE)

    suspend fun addProductToCart(product: Cart) = cartDao.insertProduct(product)

    fun setPaymentToTrue(cart: Cart) = cartDao.updateProduct(cart)

    fun pushOrdersDb(cart: Cart) =
        mFirestore.collection("Orders")
            .add(
                hashMapOf(
                    "Product" to cart.product,
                    "Quantity" to cart.quantity,
                    "Customer" to prefs.getString("Name", "Null"),
                    "Status" to OrderStatus.PENDING
                )
            ).addOnSuccessListener {
                Log.d("TAG", "pushOrdersDb: Data pushed successfully!")
                Toast.makeText(context, "Data Pushed", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Log.d(
                    "TAG",
                    "pushOrdersDb: Data cannot be pushed!: " + it.cause + " \n " + it.message
                )
                TODO("HANDLE THIS FAILED SITUATION")
            }

    fun decreaseQuantityOfProduct(cart: Cart) =
        cartDao.updateProduct(cart.copy(quantity = cart.quantity - 1))

    fun increaseQuantityOfProduct(cart: Cart) =
        cartDao.updateProduct(cart.copy(quantity = cart.quantity + 1))

    fun deleteProduct(cart: Cart) = cartDao.deleteProduct(cart)
}