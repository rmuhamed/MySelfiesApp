package com.rmuhamed.sample.myselfiesapp.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmuhamed.sample.myselfiesapp.api.RetrofitController
import com.rmuhamed.sample.myselfiesapp.api.dto.ImageDTO
import com.rmuhamed.sample.myselfiesapp.repository.GalleryRepository


class GalleryViewModel(val repo: GalleryRepository) : ViewModel() {
    val photosRetrievedLiveData = MutableLiveData<List<ImageDTO>?>(null)
    val photosNotRetrievedLiveData = MutableLiveData(false)
    val errorLiveData = MutableLiveData<String?>(null)

    var albumId: String? = null

    init {
        RetrofitController.get()
        getAlbums()
    }

    fun getAlbums() {
        repo.getAlbums(
            onError = { errorLiveData.postValue(it) },
            onSuccess = {
                if (it.isNotEmpty()) {
                    albumId = it[0].id
                    getPicturesBy(albumId = albumId!!)
                } else {
                    albumId = ""
                }
            }
        )
    }

    private fun getPicturesBy(albumId: String) {
        repo.getPictures(albumId,
            onError = { photosNotRetrievedLiveData.postValue(true) },
            onSuccess = { photosRetrievedLiveData.postValue(it) }
        )
    }

    fun existsAnAlbum() = albumId?.isNotBlank() ?: false
}