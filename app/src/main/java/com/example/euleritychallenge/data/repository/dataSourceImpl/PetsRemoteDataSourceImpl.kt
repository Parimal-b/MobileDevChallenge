package com.example.euleritychallenge.data.repository.dataSourceImpl

import com.example.euleritychallenge.data.api.PetsAPIService
import com.example.euleritychallenge.data.model.APIResponse
import com.example.euleritychallenge.data.repository.dataSource.PetsRemoteDataSource
import retrofit2.Response

class PetsRemoteDataSourceImpl(
    private val petsAPIService: PetsAPIService
): PetsRemoteDataSource {
    override suspend fun getPets(): Response<APIResponse> {
        return petsAPIService.getPets()
    }
}