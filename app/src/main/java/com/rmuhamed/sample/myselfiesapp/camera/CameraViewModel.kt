package com.rmuhamed.sample.myselfiesapp.camera

import android.util.Base64
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmuhamed.sample.myselfiesapp.repository.CameraRepository
import java.io.File

class CameraViewModel(private val repo: CameraRepository) : ViewModel() {
    val uploading = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<String?>()
    val successLiveData = MutableLiveData<Boolean?>()

    fun doUpload(file: File, name: String, title: String, description: String) {
        uploading.value = true

        val encodedImage = Base64.encodeToString(file.readBytes(), Base64.DEFAULT)

        repo.upload(
            name, title, description, encodedImage,
            onError = {
                uploading.postValue(false)
                errorLiveData.postValue(it)
            },
            onSuccess = {
                uploading.postValue(false)
                successLiveData.postValue(true)
            }
        )
    }
}