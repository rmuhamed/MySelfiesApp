package com.rmuhamed.sample.myselfiesapp.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmuhamed.sample.myselfiesapp.api.dto.ImageDTO
import com.rmuhamed.sample.myselfiesapp.repository.GalleryRepository


class GalleryViewModel(private val repo: GalleryRepository) : ViewModel() {
    val photosRetrievedLiveData = MutableLiveData<List<ImageDTO>?>(null)
    val photosNotRetrievedLiveData = MutableLiveData(false)
    val errorLiveData = MutableLiveData<String?>(null)
    val loadingLiveData = MutableLiveData<Boolean?>()

    var albumId: String? = null

    init {
        loadingLiveData.value = true
        getAlbums()
    }

    private fun getAlbums() {
        repo.getAlbums(
            onError = { errorLiveData.postValue(it) },
            onNoAlbums = {
                albumId = ""
                loadingLiveData.postValue(false)
                photosRetrievedLiveData.postValue(emptyList())
            },
            onSuccess = {
                albumId = it[0].id
                getPicturesBy(albumId = albumId!!)
            }
        )
    }

    private fun getPicturesBy(albumId: String) {
        repo.getPictures(albumId,
            onError = {
                loadingLiveData.postValue(false)
                photosNotRetrievedLiveData.postValue(true)
            },
            onSuccess = {
                loadingLiveData.postValue(false)
                photosRetrievedLiveData.postValue(it)
            }
        )
    }

    fun existsAnAlbum() = albumId?.isNotBlank() ?: false

    fun restart() {
        getAlbums()
    }
}