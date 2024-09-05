package com.example.euleritychallenge.presentation.di

import com.example.euleritychallenge.data.api.PetsAPIService
import com.example.euleritychallenge.data.repository.dataSource.PetsRemoteDataSource
import com.example.euleritychallenge.data.repository.dataSourceImpl.PetsRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteDataModule {
    @Singleton
    @Provides
    fun providePetsRemoteDataSource(
        petsAPIService: PetsAPIService
    ):PetsRemoteDataSource{
        return PetsRemoteDataSourceImpl(petsAPIService)
    }
}