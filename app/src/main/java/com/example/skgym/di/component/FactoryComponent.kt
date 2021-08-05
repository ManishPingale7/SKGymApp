package com.example.skgym.di.component

import com.example.skgym.di.modules.FactoryModule
import com.example.skgym.di.modules.RepositoryModule
import com.example.skgym.mvvm.factory.ModelFactory
import dagger.Component

@Component(modules = [FactoryModule::class, RepositoryModule::class])
interface FactoryComponent {

    fun getFactory(): ModelFactory
}