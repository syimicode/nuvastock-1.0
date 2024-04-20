package com.syimicode.nuvastock.di

import com.syimicode.nuvastock.firestorecustomer.repository.CustomerDbRepositoryImpl
import com.syimicode.nuvastock.firestorecustomer.repository.CustomerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CustomerRepositoryModule {

    @Binds
    abstract fun providesCustomerRepository(
        repo: CustomerDbRepositoryImpl
    ): CustomerRepository

}