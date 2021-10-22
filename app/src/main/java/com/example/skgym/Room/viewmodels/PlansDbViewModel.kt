package com.example.skgym.Room.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.skgym.Room.Database.PlansHisDatabase
import com.example.skgym.Room.repository.PlansHisRepo
import com.example.skgym.data.PlansDB
import kotlinx.coroutines.launch

class PlansDbViewModel(application: Application) : AndroidViewModel(application) {

    val readPlansHistory: LiveData<List<PlansDB>>
    private val repository: PlansHisRepo

    fun insertPlanToHistory(plan: PlansDB) = viewModelScope.launch {
        repository.insertPlansHis(plan)
    }

    init {
        val plansDao = PlansHisDatabase.getPlansDatabase(application).plansDao()
        repository = PlansHisRepo(plansDao, application)
        readPlansHistory = repository.readAllPlans
    }
}