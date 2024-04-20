package com.syimicode.nuvastock.di

import com.syimicode.nuvastock.firestorepeminjaman.repository.PeminjamanDbRepositoryImpl
import com.syimicode.nuvastock.firestorepeminjaman.repository.PeminjamanRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class PeminjamanRepositoryModule {

    @Binds
    abstract fun providesPeminjamanRepository(
        repo: PeminjamanDbRepositoryImpl
    ): PeminjamanRepository

}