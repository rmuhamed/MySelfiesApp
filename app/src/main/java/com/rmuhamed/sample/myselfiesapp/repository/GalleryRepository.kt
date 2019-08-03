package com.rmuhamed.sample.myselfiesapp.repository

import com.rmuhamed.sample.myselfiesapp.api.RetrofitController
import com.rmuhamed.sample.myselfiesapp.api.dto.AlbumDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.BasicResponseDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.ImageDTO
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GalleryRepository(val accessToken: String, val userName: String) {

    init {
        RetrofitController.get()
    }

    fun getAlbums(onError: (String) -> Unit, onSuccess: (List<AlbumDTO>) -> Unit) {
        RetrofitController.imgurAPI.albumsBy("Bearer $accessToken", userName, 0).enqueue(object :
            Callback<BasicResponseDTO<List<AlbumDTO>>> {
            override fun onFailure(call: Call<BasicResponseDTO<List<AlbumDTO>>>, t: Throwable) {
                onError.invoke(t.localizedMessage ?: "Unknown error")
            }

            override fun onResponse(
                call: Call<BasicResponseDTO<List<AlbumDTO>>>,
                response: Response<BasicResponseDTO<List<AlbumDTO>>>
            ) {
                response.body()?.let {
                    it.data?.let {
                        onSuccess.invoke(it)
                    } ?: run {
                        onError.invoke("No albums for this user")
                    }
                } ?: run {
                    val error = JSONObject(response.errorBody()!!.string())
                    val message = error.optJSONObject("data")?.optString("error")
                    onError.invoke(message ?: "Unknown error during Albums retrieval")
                }
            }
        })
    }

    fun getPictures(albumId: String, onError: (String) -> Unit, onSuccess: (List<ImageDTO>) -> Unit) {
        RetrofitController.imgurAPI.picturesBy("Bearer $accessToken", albumId).enqueue(object :
            Callback<BasicResponseDTO<List<ImageDTO>>> {
            override fun onFailure(call: Call<BasicResponseDTO<List<ImageDTO>>>, t: Throwable) {
                onError.invoke(t.localizedMessage ?: "Unknown error")
            }

            override fun onResponse(
                call: Call<BasicResponseDTO<List<ImageDTO>>>,
                response: Response<BasicResponseDTO<List<ImageDTO>>>
            ) {
                response.body()?.let {
                    it.data?.let { pictures ->
                        onSuccess.invoke(pictures)
                    } ?: run {
                        onSuccess.invoke(emptyList())
                    }
                } ?: run {
                    val error = JSONObject(response.errorBody()!!.string())
                    val message = error.optJSONObject("data")?.optString("error")
                    onError.invoke(message ?: "Unknown error during Pictures retrieval")
                }
            }

        })
    }
}