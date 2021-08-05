package com.example.skgym.di.modules

import com.example.skgym.mvvm.factory.ModelFactory
import com.example.skgym.mvvm.repository.BaseRepository
import dagger.Module
import dagger.Provides

@Module
class FactoryModule constructor(private var mRepository: BaseRepository) {

    @Provides
    fun provideModalFactory(): ModelFactory {
        return ModelFactory(mRepository)
    }

    @Provides
    fun providesRepository(): BaseRepository {
        return mRepository
    }
}