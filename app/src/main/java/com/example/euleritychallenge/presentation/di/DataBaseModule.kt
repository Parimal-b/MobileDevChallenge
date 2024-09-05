package com.example.euleritychallenge.presentation.di

import android.app.Application
import androidx.room.Room
import com.example.euleritychallenge.data.db.PetDao
import com.example.euleritychallenge.data.db.PetDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Singleton
    @Provides
    fun providePetDataBase(app: Application): PetDatabase {
        return Room.databaseBuilder(app, PetDatabase::class.java,"news_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePetDao(pet: PetDatabase): PetDao {
        return pet.getPetDAO()
    }
}