package com.example.skgym.di.modules

import android.content.Context
import com.example.skgym.mvvm.repository.AuthRepository
import com.example.skgym.mvvm.repository.BaseRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule constructor(private var context: Context) {

    @Provides
    fun provideRepository(): BaseRepository {
        return AuthRepository(context = context)
    }

//    @Provides
//    fun provideMainRepository(): BaseRepository {
//        return MainRepository(context = context)
//    }

}