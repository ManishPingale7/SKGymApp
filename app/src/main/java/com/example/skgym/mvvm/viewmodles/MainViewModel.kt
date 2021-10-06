package com.example.skgym.mvvm.viewmodles

import androidx.lifecycle.ViewModel
import com.example.skgym.Interfaces.BranchInterface
import com.example.skgym.Interfaces.IsMemberCallBack
import com.example.skgym.data.Member
import com.example.skgym.mvvm.repository.MainRepository

class MainViewModel constructor(var repository: MainRepository) : ViewModel() {

    fun signOut() = repository.signOut()

    fun fetchBranchNames(branchInterface: BranchInterface) = repository.fetchBranchNames(branchInterface)


    fun checkUserIsMember(branch: String,callback:IsMemberCallBack) = repository.checkUserIsMember(branch,callback)


    fun sendUserToViewPlanActivity() = repository.sendUserToViewPlanActivity()


    fun sendUserToDataActivity() = repository.sendUserToDataActivity()


    fun doesUserExists(branch: String) = repository.doesUserExists(branch)


    fun uploadUserdata(memberThis: Member) = repository.uploadUserdata(memberThis)


    fun sendUserToMainActivity() = repository.sendUserToMainActivity()


    fun isBranchExists(branch: String) = repository.doesBranchExists(branch)


    fun sendUserToHomeAuth() = repository.sendUserToHomeAuth()

    fun getAllPlans() = repository.fetchAllPlans()
    fun sendUsertogetBranchActivity() = repository.sendUsertogetBranchActivity()
//    fun changeMemberStatus()=repository.changeMemberToTrue()

    fun forgotPass(email:String)= repository.forgotPassword(email)

}