package com.rmuhamed.sample.myselfiesapp.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmuhamed.sample.myselfiesapp.BuildConfig
import com.rmuhamed.sample.myselfiesapp.api.RetrofitController
import com.rmuhamed.sample.myselfiesapp.api.dto.BasicResponseDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.ImageDTO
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GalleryViewModel : ViewModel() {

    val albumCreationLiveData = MutableLiveData(BasicResponseDTO<Int>(data = null, success = false))
    val albumNotCreatedLiveData = MutableLiveData<String?>(null)
    val albumPhotosLiveData = MutableLiveData<List<ImageDTO>>(emptyList())

    init {
        RetrofitController.get()
    }

    fun createAlbum() {
        val clientId = BuildConfig.API_CLIENT_ID
        RetrofitController.imgurAPI.createAlbum("Client-ID $clientId", "Album").enqueue(object :
            Callback<BasicResponseDTO<Int>> {
            override fun onFailure(call: Call<BasicResponseDTO<Int>>, t: Throwable) {
                albumNotCreatedLiveData.postValue(t.localizedMessage)
            }

            override fun onResponse(
                call: Call<BasicResponseDTO<Int>>,
                response: Response<BasicResponseDTO<Int>>
            ) {
                response.body()?.let {
                    if (it.success) {
                        albumCreationLiveData.postValue(response.body())
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