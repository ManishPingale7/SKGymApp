package com.example.skgym.mvvm.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skgym.mvvm.repository.BaseRepository

@Suppress("UNCHECKED_CAST")
class ModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
//            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
//                AuthViewModel(repository as AuthRepository) as T
//            }
//            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
//                MainViewModel(repository = repository as MainRepository) as T
//            }
            else -> {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }

}