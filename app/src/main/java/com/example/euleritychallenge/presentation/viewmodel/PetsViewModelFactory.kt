package com.example.euleritychallenge.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.euleritychallenge.domain.usecase.DeletePetUseCase
import com.example.euleritychallenge.domain.usecase.GetPetsUseCase
import com.example.euleritychallenge.domain.usecase.GetSavedPetsUseCase
import com.example.euleritychallenge.domain.usecase.SavePetUseCase

class PetsViewModelFactory(
    private val app: Application,
    val getPetsUseCase: GetPetsUseCase,
    val savePetUseCase: SavePetUseCase,
    val getSavedPetsUseCase: GetSavedPetsUseCase,
    val deletePetUseCase: DeletePetUseCase
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PetsViewModel(app, getPetsUseCase, savePetUseCase, getSavedPetsUseCase, deletePetUseCase) as T
    }

}