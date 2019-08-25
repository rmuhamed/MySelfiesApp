package com.rmuhamed.sample.myselfiesapp.camera

import android.util.Base64
import androidx.camera.core.ImageCapture
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmuhamed.sample.myselfiesapp.repository.CameraRepository
import java.io.File

class CameraViewModel(private val repo: CameraRepository) : ViewModel() {
    val uploadingLiveData = MutableLiveData<Boolean>()
    val someErrorLiveData = MutableLiveData<String?>()
    val uploadedLiveData = MutableLiveData<Boolean?>()

    val capturedPictureSucceed = MutableLiveData<File?>()

    fun uploadPicture(file: File, name: String, title: String, description: String) {
        uploadingLiveData.value = true

        val encodedImage = Base64.encodeToString(file.readBytes(), Base64.DEFAULT)

        repo.upload(
            name, title, description, encodedImage,
            onError = {
                uploadingLiveData.postValue(false)
                someErrorLiveData.postValue(it)
            },
            onSuccess = {
                uploadingLiveData.postValue(false)
                uploadedLiveData.postValue(true)
            }
        )
    }

    fun captureNewPicture(imageCapture: ImageCapture, timeStamp: Long, root: File) {
        val fileName = "${repo.albumId + '_' + timeStamp}.jpg"
        val pictureFile = File(root, fileName)

        imageCapture.takePicture(pictureFile,
            object : ImageCapture.OnImageSavedListener {
                override fun onError(
                    error: ImageCapture.UseCaseError,
                    message: String, exc: Throwable?
                ) {
                    someErrorLiveData.postValue("Photo capture failed: $message")
                }

                override fun onImageSaved(file: File) {
                    capturedPictureSucceed.postValue(file)
                }
            })
    }
}