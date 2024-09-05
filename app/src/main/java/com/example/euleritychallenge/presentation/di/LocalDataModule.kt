package com.example.euleritychallenge.presentation.di

import com.example.euleritychallenge.data.db.PetDao
import com.example.euleritychallenge.data.repository.dataSource.PetsLocalDataSource
import com.example.euleritychallenge.data.repository.dataSourceImpl.PetsLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {
    @Singleton
    @Provides
    fun provideLocalDataSource(petDao: PetDao): PetsLocalDataSource {
        return PetsLocalDataSourceImpl(petDao)
    }
}