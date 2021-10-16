package com.example.skgym.Databases.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.skgym.Databases.Database.CartDatabase
import com.example.skgym.Databases.repository.CartDatabaseRepository
import com.example.skgym.data.Cart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val readAllData: LiveData<List<Cart>>
    private val repository: CartDatabaseRepository

    init {
        val cartDao = CartDatabase.getDatabase(application).cartDao()
        repository = CartDatabaseRepository(cartDao, application)
        readAllData = repository.readAllData
    }

    fun addProductToCartDB(product: Cart) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addProductToCart(product)
        }
    }
}