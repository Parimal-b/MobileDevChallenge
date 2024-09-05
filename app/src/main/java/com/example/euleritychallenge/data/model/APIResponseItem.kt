package com.example.euleritychallenge.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity(
    tableName = "pets"
)
data class APIResponseItem(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,
    @SerializedName("created")
    val created: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
): Serializable