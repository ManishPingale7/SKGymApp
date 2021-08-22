package com.example.skgym.mvvm.viewmodles

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.skgym.mvvm.repository.MainRepository

class MainViewModel constructor(var repository: MainRepository) : ViewModel() {

    fun signOut() {
        repository.signOut()
    }

    fun fetchBranchNames(): MutableLiveData<ArrayList<String>> {
        return repository.fetchBranchNames()
    }

    fun checkUserIsMember(branch: String): Boolean {
        return repository.checkUserIsMember(branch)
    }

    fun sendUserToViewPlanActivity() {
        repository.sendUserToViewPlanActivity()
    }
}