package com.example.skgym.mvvm.viewmodles

import androidx.lifecycle.ViewModel
import com.example.skgym.data.Member
import com.example.skgym.mvvm.repository.MainRepository

class MainViewModel constructor(var repository: MainRepository) : ViewModel() {

    fun signOut() = repository.signOut()

    fun fetchBranchNames() = repository.fetchBranchNames()


    fun checkUserIsMember(branch: String) = repository.checkUserIsMember(branch)


    fun sendUserToViewPlanActivity() = repository.sendUserToViewPlanActivity()


    fun sendUserToDataActivity() = repository.sendUserToDataActivity()


    fun doesUserExists(branch: String) = repository.doesUserExists(branch)


    fun uploadUserdata(memberThis: Member) = repository.uploadUserdata(memberThis)


    fun sendUserToMainActivity() = repository.sendUserToMainActivity()


    fun isBranchExists(branch: String) = repository.doesBranchExists(branch)


    fun sendUserToHomeAuth() = repository.sendUserToHomeAuth()

    fun getAllPlans() = repository.fetchAllPlans()


}