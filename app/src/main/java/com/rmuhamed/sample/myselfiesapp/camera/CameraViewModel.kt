package com.rmuhamed.sample.myselfiesapp.camera

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmuhamed.sample.myselfiesapp.BuildConfig
import com.rmuhamed.sample.myselfiesapp.api.RetrofitController
import com.rmuhamed.sample.myselfiesapp.api.dto.BasicResponseDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.UploadImageRequestDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.UploadedImageDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class CameraViewModel : ViewModel() {
    private var accessToken = BuildConfig.ACCESS_TOKEN
    val uploading = MutableLiveData<Boolean>()

    init {
        RetrofitController.get()
    }

    fun upload(albumId: String, file: File) {
        uploading.value = true

        val dto = UploadImageRequestDTO(
            Base64.getEncoder().encodeToString(file.readBytes()),
            albumId,
            "image",
            "Picture",
            "A Picture",
            "A description"
        )

        RetrofitController.imgurAPI.uploadPhoto("Bearer $accessToken", dto).enqueue(object :
            Callback<BasicResponseDTO<UploadedImageDTO>> {
            override fun onFailure(call: Call<BasicResponseDTO<UploadedImageDTO>>, t: Throwable) {
                uploading.postValue(false)
            }

            override fun onResponse(
                call: Call<BasicResponseDTO<UploadedImageDTO>>,
                response: Response<BasicResponseDTO<UploadedImageDTO>>
            ) {
                uploading.postValue(false)
            }
        })
    }
}