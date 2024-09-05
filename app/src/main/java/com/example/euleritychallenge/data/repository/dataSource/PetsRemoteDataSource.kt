package com.example.euleritychallenge.data.repository.dataSource

import com.example.euleritychallenge.data.model.APIResponse
import retrofit2.Response

interface PetsRemoteDataSource {

    suspend fun getPets(): Response<APIResponse>
}