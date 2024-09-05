package com.example.euleritychallenge.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.euleritychallenge.data.model.APIResponseItem

@Database(
    entities = [APIResponseItem::class],
    version = 1,
    exportSchema = false
)
@TypeConverters()
abstract class PetDatabase : RoomDatabase() {
    abstract fun getPetDAO():PetDao
}