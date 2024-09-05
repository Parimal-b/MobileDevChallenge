package com.example.euleritychallenge.data.repository

import com.example.euleritychallenge.data.model.APIResponse
import com.example.euleritychallenge.data.model.APIResponseItem
import com.example.euleritychallenge.data.repository.dataSource.PetsLocalDataSource
import com.example.euleritychallenge.data.repository.dataSource.PetsRemoteDataSource
import com.example.euleritychallenge.data.util.Resource
import com.example.euleritychallenge.domain.repository.PetsRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class PetsRepositoryImpl(
    private val petsRemoteDataSource: PetsRemoteDataSource,
    private val petsLocalDataSource: PetsLocalDataSource
): PetsRepository {

    private fun responseToResource(response: Response<APIResponse>): Resource<APIResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    override suspend fun getPets(): Resource<APIResponse> {
        return responseToResource(petsRemoteDataSource.getPets())
    }

    override suspend fun getSearchedPets(query: String): Resource<APIResponseItem> {
        TODO("Not yet implemented")
    }

    override suspend fun savePets(pet: APIResponseItem) {
        petsLocalDataSource.savePetsToDB(pet)
    }

    override suspend fun deletePet(pet: APIResponseItem) {
        petsLocalDataSource.deletePetArticlesFromDB(pet)
    }

    override fun getSavedPets(): Flow<List<APIResponseItem>> {
        return petsLocalDataSource.getSavedPets()
    }
}