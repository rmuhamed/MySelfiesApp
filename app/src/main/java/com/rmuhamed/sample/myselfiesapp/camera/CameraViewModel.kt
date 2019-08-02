package com.rmuhamed.sample.myselfiesapp.camera

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmuhamed.sample.myselfiesapp.repository.CameraRepository
import java.io.File
import java.util.*

class CameraViewModel(private val repo: CameraRepository) : ViewModel() {
    val uploading = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<String?>()
    val successLiveData = MutableLiveData<Boolean?>()

    fun doUpload(file: File, name: String, title: String, description: String) {
        uploading.value = true

        val base64 = Base64.getEncoder().encodeToString(file.readBytes())

        repo.upload(name, title, description, base64,
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