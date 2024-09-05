package com.example.euleritychallenge.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.euleritychallenge.data.model.APIResponseItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pet: APIResponseItem)

    @Query("SELECT * FROM pets")
    fun getAllArticles(): Flow<List<APIResponseItem>>

    @Delete
    suspend fun deletePet(pet: APIResponseItem)

}