package com.example.euleritychallenge.domain.usecase

import com.example.euleritychallenge.data.model.APIResponse
import com.example.euleritychallenge.data.util.Resource
import com.example.euleritychallenge.domain.repository.PetsRepository

class GetPetsUseCase(private val petsRepository: PetsRepository) {

    suspend fun execute(): Resource<APIResponse> {
        return petsRepository.getPets()
    }
}