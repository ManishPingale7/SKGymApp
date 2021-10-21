package com.example.skgym.Room.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skgym.data.PlansDB

@Dao
interface PlansDbDao {

    //Insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plansDb: PlansDB)

    //Update - No need
    //Delete - No need

    //Retrieve
    @Query("SELECT * FROM PlansDB ORDER BY id DESC")
    fun retrievePlansHistory(): LiveData<List<PlansDB>>
}