package com.rmuhamed.sample.myselfiesapp.camera

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmuhamed.sample.myselfiesapp.BuildConfig
import com.rmuhamed.sample.myselfiesapp.api.RetrofitController
import com.rmuhamed.sample.myselfiesapp.api.dto.UploadImageRequestDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.UploadImageResponseDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class CameraViewModel : ViewModel() {
    private var clientId = BuildConfig.API_CLIENT_ID
    val uploading = MutableLiveData<Boolean>()

    init {
        RetrofitController.get()
    }

    fun upload(file: File) {
        uploading.value = true

        val dto = UploadImageRequestDTO(
            Base64.getEncoder().encodeToString(file.readBytes()),
            "image",
            "Picture",
            "A Picture",
            "A description"
        )

        RetrofitController.imgurAPI.uploadPhoto("Client-ID $clientId", dto).enqueue(object :
            Callback<UploadImageResponseDTO> {
            override fun onFailure(call: Call<UploadImageResponseDTO>, t: Throwable) {
                uploading.postValue(false)
            }

            override fun onResponse(
                call: Call<UploadImageResponseDTO>,
                response: Response<UploadImageResponseDTO>
            ) {
                uploading.postValue(false)
            }
        })
    }
}