package com.example.skgym.Room.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.skgym.Room.Dao.CartDao
import com.example.skgym.data.Cart
import com.example.skgym.mvvm.repository.BaseRepository

class CartDatabaseRepository(private val cartDao: CartDao, var context: Context) :
    BaseRepository(context) {

    val readUnpaidCartData: LiveData<List<Cart>> = cartDao.getUnpaidCart()
    val readAllData: LiveData<List<Cart>> = cartDao.getCart()

    suspend fun addProductToCart(product: Cart) = cartDao.insertProduct(product)
    suspend fun setPaymentToTrue(cart: Cart) = cartDao.updateProduct(cart)

}