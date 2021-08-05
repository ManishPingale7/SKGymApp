package com.example.skgym.mvvm.viewmodles

import androidx.lifecycle.ViewModel
import com.example.skgym.mvvm.repository.MainRepository

class MainViewModel constructor(var repository: MainRepository) : ViewModel() {

    fun signOut() {
        repository.signOut()
    }
}