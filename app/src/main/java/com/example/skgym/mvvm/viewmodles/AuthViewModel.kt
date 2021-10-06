package com.example.skgym.mvvm.viewmodles

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.skgym.Interfaces.BranchInterface
import com.example.skgym.mvvm.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class AuthViewModel(
    val repository: AuthRepository
) : ViewModel() {

    fun login(email: String, password: String) = CoroutineScope(IO).launch {
        repository.login(email, password)
    }

    fun register(email: String, password: String) = CoroutineScope(IO).launch {
        repository.register(email, password)
    }



    fun sendUserToMainActivity() {
        repository.sendUserToMainActivity()
    }

    fun fetchBranchNames(branchInterface: BranchInterface): MutableLiveData<ArrayList<String>> {
        return repository.fetchBranchNames(branchInterface)
    }


}