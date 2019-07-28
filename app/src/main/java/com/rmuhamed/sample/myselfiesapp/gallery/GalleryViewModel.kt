package com.rmuhamed.sample.myselfiesapp.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmuhamed.sample.myselfiesapp.BuildConfig
import com.rmuhamed.sample.myselfiesapp.api.RetrofitController
import com.rmuhamed.sample.myselfiesapp.api.dto.BasicResponseDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.CreateAlbumRequestDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.CreatedAlbumDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.ImageDTO
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GalleryViewModel : ViewModel() {

    val albumCreationLiveData = MutableLiveData<String?>(null)
    val albumNotCreatedLiveData = MutableLiveData<String?>(null)
    val albumPhotosLiveData = MutableLiveData<List<ImageDTO>?>(null)

    init {
        RetrofitController.get()
    }

    fun createAlbum() {
        val clientId = BuildConfig.API_CLIENT_ID
        val requestDTO = CreateAlbumRequestDTO("Album", "Description")
        RetrofitController.imgurAPI.createAlbum("Client-ID $clientId", requestDTO).enqueue(object :
            Callback<BasicResponseDTO<CreatedAlbumDTO>> {
            override fun onFailure(call: Call<BasicResponseDTO<CreatedAlbumDTO>>, t: Throwable) {
                albumNotCreatedLiveData.postValue(t.localizedMessage)
            }

            override fun onResponse(
                call: Call<BasicResponseDTO<CreatedAlbumDTO>>,
                response: Response<BasicResponseDTO<CreatedAlbumDTO>>
            ) {
                response.body()?.let {
                    if (it.success) {
                        albumCreationLiveData.postValue(it.data!!.id)
                    }
                } ?: run {
                    val error = JSONObject(response.errorBody()!!.string())
                    val message = error.optJSONObject("data")?.optString("error")

                    albumNotCreatedLiveData.postValue(message ?: "Album couldn't be created")
                }
            }
        })
    }
}