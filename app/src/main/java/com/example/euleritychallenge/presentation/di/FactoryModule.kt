package com.example.euleritychallenge.presentation.di

import android.app.Application
import com.example.euleritychallenge.domain.usecase.DeletePetUseCase
import com.example.euleritychallenge.domain.usecase.GetPetsUseCase
import com.example.euleritychallenge.domain.usecase.GetSavedPetsUseCase
import com.example.euleritychallenge.domain.usecase.SavePetUseCase
import com.example.euleritychallenge.presentation.viewmodel.PetsViewModel
import com.example.euleritychallenge.presentation.viewmodel.PetsViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {

    @Singleton
    @Provides
    fun providePetsViewModelFactory(
        application: Application,
        getPetsUseCase: GetPetsUseCase,
        getSavedPetsUseCase: GetSavedPetsUseCase,
        savePetUseCase: SavePetUseCase,
        deletePetUseCase: DeletePetUseCase
    ): PetsViewModelFactory {
        return PetsViewModelFactory(application, getPetsUseCase,savePetUseCase, getSavedPetsUseCase, deletePetUseCase)
    }
}