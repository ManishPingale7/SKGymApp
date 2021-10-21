package com.example.skgym.Room.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.skgym.Room.Database.PlansHisDatabase
import com.example.skgym.Room.repository.PlansHisRepo
import com.example.skgym.data.PlansDB

class PlansDbViewModel(application: Application) : AndroidViewModel(application) {
    val readPlansHistory: LiveData<List<PlansDB>>
    private val repository: PlansHisRepo

    init {
        val plansDao = PlansHisDatabase.getPlansDatabase(application).plansDao()
        repository = PlansHisRepo(plansDao, application)
        readPlansHistory = repository.readAllPlans
    }
}