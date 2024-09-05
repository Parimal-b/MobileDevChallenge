package com.example.euleritychallenge.data.repository.dataSource

import com.example.euleritychallenge.data.model.APIResponseItem
import kotlinx.coroutines.flow.Flow

interface PetsLocalDataSource {
    suspend fun savePetsToDB(pet: APIResponseItem)

    fun getSavedPets(): Flow<List<APIResponseItem>>

    suspend fun deletePetArticlesFromDB(pet: APIResponseItem)

}