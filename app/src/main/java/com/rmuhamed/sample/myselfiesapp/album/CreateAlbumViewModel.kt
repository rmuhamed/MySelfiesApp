package com.rmuhamed.sample.myselfiesapp.album

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmuhamed.sample.myselfiesapp.repository.CreateAlbumRepository

class CreateAlbumViewModel(val repo: CreateAlbumRepository) : ViewModel() {
    val albumCreationLiveData = MutableLiveData<String?>(null)
    val albumNotCreatedLiveData = MutableLiveData<String?>(null)

    fun createAlbum(title: String, description: String) {
        repo.createAlbum(title,
            description,
            onError = { albumNotCreatedLiveData.postValue(it) },
            onSuccess = { albumCreationLiveData.postValue(it) }
        )
    }
}