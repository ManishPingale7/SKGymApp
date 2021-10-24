package com.example.skgym.mvvm.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.skgym.data.ProductCategory
import com.example.skgym.utils.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainRepository(private var contextMain: Context) : BaseRepository(contextMain) {

    private var categories = MutableLiveData<ArrayList<ProductCategory>>()
    private val mDatabase = FirebaseDatabase.getInstance()
    private val categoryInfo = mDatabase.getReference(Constants.CATEGORYINFO)


    fun getCategoriesInfo(): MutableLiveData<ArrayList<ProductCategory>> {
        val tempList = ArrayList<ProductCategory>(40)

        categoryInfo.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                categories.value?.clear()
                dataSnapshot.children.forEach {
                    Log.d(TAG, "onDataChange: New Fetched$it")
                    tempList.add(it.getValue(ProductCategory::class.java)!!)
                }
                categories.value = tempList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCreateView123 onCancelled: $error")
            }
        })
        return categories
    }


}