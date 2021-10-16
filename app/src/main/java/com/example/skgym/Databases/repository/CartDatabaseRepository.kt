package com.example.skgym.Databases.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.skgym.Databases.Dao.CartDao
import com.example.skgym.data.Cart
import com.example.skgym.mvvm.repository.BaseRepository

class CartDatabaseRepository(private val cartDao: CartDao, var context: Context) :
    BaseRepository(context) {

    val readAllData: LiveData<List<Cart>> = cartDao.getCart()

    suspend fun addProductToCart(product: Cart) = cartDao.insertProduct(product)

}