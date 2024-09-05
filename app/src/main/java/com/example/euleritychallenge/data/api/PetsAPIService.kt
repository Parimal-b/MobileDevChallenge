package com.example.euleritychallenge.data.api

import com.example.euleritychallenge.data.model.APIResponse
import retrofit2.Response
import retrofit2.http.GET

interface PetsAPIService {

    @GET("pets")
    suspend fun getPets(): Response<APIResponse>
}