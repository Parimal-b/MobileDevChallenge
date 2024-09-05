package com.example.euleritychallenge.presentation.di

import com.example.euleritychallenge.data.repository.PetsRepositoryImpl
import com.example.euleritychallenge.data.repository.dataSource.PetsLocalDataSource
import com.example.euleritychallenge.data.repository.dataSource.PetsRemoteDataSource
import com.example.euleritychallenge.domain.repository.PetsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun provideNewsRepository(
        petsRemoteDataSource: PetsRemoteDataSource,
        petsLocalDataSource: PetsLocalDataSource
    ): PetsRepository {
        return PetsRepositoryImpl(petsRemoteDataSource, petsLocalDataSource)
    }
}