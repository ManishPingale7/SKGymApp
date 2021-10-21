package com.example.skgym.Room.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.skgym.Room.Dao.PlansDbDao
import com.example.skgym.data.PlansDB
import com.example.skgym.mvvm.repository.BaseRepository

class PlansHisRepo(private val plansDbDao: PlansDbDao, var context: Context) :
    BaseRepository(context) {

    val readAllPlans: LiveData<List<PlansDB>> = plansDbDao.retrievePlansHistory()

    suspend fun insertPlansHis(plansDB: PlansDB) = plansDbDao.insertPlan(plansDB)
}