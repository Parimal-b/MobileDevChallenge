package com.example.euleritychallenge.domain.usecase

import com.example.euleritychallenge.data.model.APIResponse
import com.example.euleritychallenge.data.model.APIResponseItem
import com.example.euleritychallenge.data.util.Resource
import com.example.euleritychallenge.domain.repository.PetsRepository

class GetSearchedPetsUseCase(private val petsRepository: PetsRepository) {

    suspend fun execute(searchQuery: String): Resource<APIResponseItem>{
        return petsRepository.getSearchedPets(searchQuery)
    }
}