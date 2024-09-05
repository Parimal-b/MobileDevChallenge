package com.example.euleritychallenge.presentation.di

import com.example.euleritychallenge.domain.repository.PetsRepository
import com.example.euleritychallenge.domain.usecase.DeletePetUseCase
import com.example.euleritychallenge.domain.usecase.GetPetsUseCase
import com.example.euleritychallenge.domain.usecase.GetSavedPetsUseCase
import com.example.euleritychallenge.domain.usecase.SavePetUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun providePetsUseCase(
        petsRepository: PetsRepository
    ): GetPetsUseCase {
        return GetPetsUseCase(petsRepository)
    }

    @Singleton
    @Provides
    fun provideSavePetsCaseUseCase(
        petRepository: PetsRepository
    ): SavePetUseCase {
        return SavePetUseCase(petRepository)
    }

    @Singleton
    @Provides
    fun provideGetSavedPetsUseCase(
        petRepository: PetsRepository
    ): GetSavedPetsUseCase{
        return GetSavedPetsUseCase(petRepository)
    }

    @Singleton
    @Provides
    fun provideDeletePetUseCase(
        petsRepository: PetsRepository
    ): DeletePetUseCase{
        return DeletePetUseCase(petsRepository)
    }


}