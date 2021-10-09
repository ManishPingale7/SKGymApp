package com.example.skgym.mvvm.viewmodles

import androidx.lifecycle.ViewModel
import com.example.skgym.Interfaces.BranchInterface
import com.example.skgym.Interfaces.DataAdded
import com.example.skgym.Interfaces.IsMemberCallBack
import com.example.skgym.data.Member
import com.example.skgym.mvvm.repository.MainRepository

class MainViewModel constructor(var repository: MainRepository) : ViewModel() {


    val allCategories = repository.getCategoriesInfo()
    fun signOut() = repository.signOut()

    fun fetchBranchNames(branchInterface: BranchInterface) =
        repository.fetchBranchNames(branchInterface)


    fun checkUserIsMember(branch: String, callback: IsMemberCallBack) =
        repository.checkUserIsMember(branch, callback)


    fun sendUserToViewPlanActivity() = repository.sendUserToViewPlanActivity()


    fun uploadUserdata(memberThis: Member,dataAdded: DataAdded) = repository.uploadUserdata(memberThis,dataAdded)


    fun sendUserToMainActivity() = repository.sendUserToMainActivity()


    fun isBranchExists(branch: String) = repository.doesUserAndBranchExists(branch)


    fun sendUserToHomeAuth() = repository.sendUserToHomeAuth()

    fun getAllPlans() = repository.fetchAllPlans()
    fun sendUsertogetBranchActivity() = repository.sendUsertogetBranchActivity()
//    fun changeMemberStatus()=repository.changeMemberToTrue()

    fun forgotPass(email: String) = repository.forgotPassword(email)

    fun loadProducts(name: String) = repository.loadAllProducts(name)

}