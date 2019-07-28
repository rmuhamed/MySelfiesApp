package com.rmuhamed.sample.myselfiesapp.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmuhamed.sample.myselfiesapp.BuildConfig
import com.rmuhamed.sample.myselfiesapp.api.RetrofitController
import com.rmuhamed.sample.myselfiesapp.api.dto.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GalleryViewModel : ViewModel() {

    val albumCreationLiveData = MutableLiveData<String?>(null)
    val albumNotCreatedLiveData = MutableLiveData<String?>(null)
    val albumPhotosLiveData = MutableLiveData<List<ImageDTO>?>(null)

    private val accessToken = BuildConfig.ACCESS_TOKEN

    lateinit var albumId: String

    init {
        RetrofitController.get()
        getAlbums()
    }

    private fun getAlbums() {
        RetrofitController.imgurAPI.albumsBy("Bearer $accessToken", "rmuhamed", 0).enqueue(object :
            Callback<BasicResponseDTO<List<AlbumDTO>>> {
            override fun onFailure(call: Call<BasicResponseDTO<List<AlbumDTO>>>, t: Throwable) {
                albumPhotosLiveData.postValue(null)
            }

            override fun onResponse(
                call: Call<BasicResponseDTO<List<AlbumDTO>>>,
                response: Response<BasicResponseDTO<List<AlbumDTO>>>
            ) {
                response.body()?.let {
                    albumId = it.data!![0].id
                    getAlbumPictures()
                } ?: run {
                    val error = JSONObject(response.errorBody()!!.string())
                    val message = error.optJSONObject("data")?.optString("error")
                    albumPhotosLiveData.postValue(null)
                }
            }
        })
    }

    fun getAlbumPictures() {
        RetrofitController.imgurAPI.picturesBy("Bearer $accessToken", albumId).enqueue(object :
            Callback<BasicResponseDTO<List<ImageDTO>>> {
            override fun onFailure(call: Call<BasicResponseDTO<List<ImageDTO>>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<BasicResponseDTO<List<ImageDTO>>>,
                response: Response<BasicResponseDTO<List<ImageDTO>>>
            ) {
                response.body()?.let {
                    albumPhotosLiveData.postValue(response.body()!!.data)
                }
            }

        })
    }

    fun existsAnAlbum() = albumId.isNotBlank()

    fun createAlbum() {
        val requestDTO = CreateAlbumRequestDTO("Album", "Description")
        RetrofitController.imgurAPI.createAlbum("Bearer $accessToken", requestDTO).enqueue(object :
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