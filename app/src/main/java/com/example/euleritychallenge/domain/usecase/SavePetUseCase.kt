package com.example.euleritychallenge.domain.usecase

import com.example.euleritychallenge.data.model.APIResponseItem
import com.example.euleritychallenge.domain.repository.PetsRepository

class SavePetUseCase(
    private val petsRepository: PetsRepository
) {
    suspend fun execute(pet: APIResponseItem) = petsRepository.savePets(pet)


}