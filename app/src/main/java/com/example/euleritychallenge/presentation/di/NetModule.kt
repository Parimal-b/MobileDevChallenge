package com.example.euleritychallenge.presentation.di

import com.example.euleritychallenge.BuildConfig
import com.example.euleritychallenge.data.api.PetsAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.MY_BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun providePetsAPIService(retrofit: Retrofit): PetsAPIService{
        return retrofit.create(PetsAPIService::class.java)
    }
}