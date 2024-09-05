package com.example.euleritychallenge.domain.repository

import com.example.euleritychallenge.data.model.APIResponse
import com.example.euleritychallenge.data.model.APIResponseItem
import com.example.euleritychallenge.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface PetsRepository {

    suspend fun getPets(): Resource<APIResponse>
    suspend fun getSearchedPets(query: String): Resource<APIResponseItem>
    suspend fun savePets(pet: APIResponseItem)
    suspend fun deletePet(pet: APIResponseItem)
    fun getSavedPets(): Flow<List<APIResponseItem>>
}