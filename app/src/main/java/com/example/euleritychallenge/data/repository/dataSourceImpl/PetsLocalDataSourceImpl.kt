package com.example.euleritychallenge.data.repository.dataSourceImpl

import com.example.euleritychallenge.data.db.PetDao
import com.example.euleritychallenge.data.model.APIResponseItem
import com.example.euleritychallenge.data.repository.dataSource.PetsLocalDataSource
import kotlinx.coroutines.flow.Flow

class PetsLocalDataSourceImpl(
    private val petDao: PetDao
) : PetsLocalDataSource {
    override suspend fun savePetsToDB(pet: APIResponseItem) {
        petDao.insert(pet)
    }

    override fun getSavedPets(): Flow<List<APIResponseItem>> {
        return petDao.getAllArticles()
    }

    override suspend fun deletePetArticlesFromDB(pet: APIResponseItem) {
        petDao.deletePet(pet)
    }
}