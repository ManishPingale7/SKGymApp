package com.example.skgym.Room.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.skgym.data.Cart

@Dao
interface CartDao {

    //Insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Cart)

    //Delete
    @Delete
    fun deleteProduct(cart: Cart)

    //Update
    @Update
    suspend fun updateProduct(cart: Cart)

    //QueryAll
    @Query("SELECT * FROM Cart")
    fun getCart(): LiveData<List<Cart>>

    //Get unpaid Products only
    @Query("SELECT * FROM Cart WHERE paymentDone IS 0")
    fun getUnpaidCart(): LiveData<List<Cart>>
}