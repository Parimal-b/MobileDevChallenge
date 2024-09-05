package com.example.euleritychallenge.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.euleritychallenge.data.model.APIResponse
import com.example.euleritychallenge.data.model.APIResponseItem
import com.example.euleritychallenge.data.util.Resource
import com.example.euleritychallenge.domain.usecase.DeletePetUseCase
import com.example.euleritychallenge.domain.usecase.GetPetsUseCase
import com.example.euleritychallenge.domain.usecase.GetSavedPetsUseCase
import com.example.euleritychallenge.domain.usecase.SavePetUseCase
import kotlinx.coroutines.launch

class PetsViewModel(
    val app: Application,
    val getPetsUseCase: GetPetsUseCase,
    val savePetUseCase: SavePetUseCase,
    val getSavedPetsUseCase: GetSavedPetsUseCase,
    val deletePetUseCase: DeletePetUseCase
): ViewModel() {

    var pets: MutableLiveData<Resource<APIResponse>> = MutableLiveData()
    var isSortAscending = true
    val savedPets: MutableLiveData<List<APIResponseItem>> = MutableLiveData()
    private val _savePetStatus = MutableLiveData<SaveStatus>()
    val savePetStatus: LiveData<SaveStatus> = _savePetStatus


    fun getPets() = viewModelScope.launch {
        pets.postValue(Resource.Loading())
        val apiResult = getPetsUseCase.execute()
        if (apiResult is Resource.Success && apiResult.data != null) {
            val sortedPets = if (isSortAscending) apiResult.data.sortedBy { it.title } else apiResult.data.sortedByDescending { it.title }
            val sortedApiResponse = APIResponse().apply {
                addAll(sortedPets)
            }
            pets.postValue(Resource.Success(sortedApiResponse))
        } else {
            pets.postValue(apiResult as Resource<APIResponse>)
        }
    }

    fun getCombinedPets() = viewModelScope.launch {
        pets.postValue(Resource.Loading())

        val apiResult = getPetsUseCase.execute()
        val savedPetsResult = getSavedPetsUseCase.execute()

        savedPetsResult.collect { savedPets ->
            if (apiResult is Resource.Success && apiResult.data != null) {

                val allPets = mutableListOf<APIResponseItem>()
                allPets.addAll(apiResult.data)
                allPets.addAll(savedPets)

                val uniquePets = allPets.distinctBy { it.url }


                val sortedPets = if (isSortAscending) {
                    uniquePets.sortedBy { it.title }
                } else {
                    uniquePets.sortedByDescending { it.title }
                }

                val sortedApiResponse = APIResponse().apply {
                    addAll(sortedPets)
                }
                pets.postValue(Resource.Success(sortedApiResponse))
            } else {
                pets.postValue(apiResult as Resource<APIResponse>)
            }
        }
    }


    fun savePet(pet: APIResponseItem) {

        viewModelScope.launch {
            getSavedPets()
            val isPetAlreadySaved = savedPets.value?.any {
                it.title == pet.title && it.description == pet.description && it.url == pet.url
            } ?: false

            val success = if (!isPetAlreadySaved) {
                savePetUseCase.execute(pet)
                true
            } else {
                false
            }
            _savePetStatus.postValue(SaveStatus(success))
        }
    }



    fun getSavedPets() = viewModelScope.launch {
        getSavedPetsUseCase.execute().collect { savedPetsList ->
            savedPets.postValue(savedPetsList)
        }
    }



    fun isIdExistsInDb(id: Int): Boolean {
        return pets.value?.data?.any { it.id == id } ?: false
    }

    fun clearSavePetStatus() {
        _savePetStatus.value = null
    }

    fun deletePetFromDB(pet: APIResponseItem){
        viewModelScope.launch {
            deletePetUseCase.execute(pet)
            getSavedPets()
        }
    }
}

data class SaveStatus(val success: Boolean)