package com.example.euleritychallenge.domain.usecase

import com.example.euleritychallenge.data.model.APIResponseItem
import com.example.euleritychallenge.domain.repository.PetsRepository
import kotlinx.coroutines.flow.Flow

class GetSavedPetsUseCase(
    private val petsRepository: PetsRepository
) {

    fun execute(): Flow<List<APIResponseItem>> {
        return petsRepository.getSavedPets()
    }
}