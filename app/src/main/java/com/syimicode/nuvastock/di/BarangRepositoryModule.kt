package com.syimicode.nuvastock.di

import com.syimicode.nuvastock.firestorebarang.repository.BarangDbRepositoryImpl
import com.syimicode.nuvastock.firestorebarang.repository.BarangRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class BarangRepositoryModule {

    @Binds
    abstract fun providesBarangRepository(
        repo: BarangDbRepositoryImpl
    ): BarangRepository

}