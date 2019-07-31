package com.rmuhamed.sample.myselfiesapp.repository

import com.rmuhamed.sample.myselfiesapp.api.RetrofitController
import com.rmuhamed.sample.myselfiesapp.api.dto.*
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

    fun createAlbum(title: String, description: String, onError: (String) -> Unit, onSuccess: (String) -> Unit) {
        val requestDTO = CreateAlbumRequestDTO(title, description)
        RetrofitController.imgurAPI.createAlbum("Bearer $accessToken", requestDTO).enqueue(object :
            Callback<BasicResponseDTO<CreatedAlbumDTO>> {
            override fun onFailure(call: Call<BasicResponseDTO<CreatedAlbumDTO>>, t: Throwable) {
                onError.invoke(t.localizedMessage ?: "Unknown error")
            }

            override fun onResponse(
                call: Call<BasicResponseDTO<CreatedAlbumDTO>>,
                response: Response<BasicResponseDTO<CreatedAlbumDTO>>
            ) {
                response.body()?.let {
                    it.data?.let { dto ->
                        onSuccess.invoke(dto.id)
                    }
                } ?: run {
                    val error = JSONObject(response.errorBody()!!.string())
                    val message = error.optJSONObject("data")?.optString("error")
                    onError.invoke(message ?: "Album couldn't be created")
                }
            }
        })
    }
}