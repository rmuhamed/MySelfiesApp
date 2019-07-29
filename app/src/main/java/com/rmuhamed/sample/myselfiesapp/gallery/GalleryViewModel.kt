package com.rmuhamed.sample.myselfiesapp.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmuhamed.sample.myselfiesapp.api.RetrofitController
import com.rmuhamed.sample.myselfiesapp.api.dto.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GalleryViewModel : ViewModel() {

    val albumCreationLiveData = MutableLiveData<String?>(null)
    val albumNotCreatedLiveData = MutableLiveData<String?>(null)
    val photosRetrievedLiveData = MutableLiveData<List<ImageDTO>?>(null)
    val photosNotRetrievedLiveData = MutableLiveData(false)

    lateinit var albumId: String

    init {
        RetrofitController.get()
    }

    fun getAlbums(accessToken: String) {
        RetrofitController.imgurAPI.albumsBy("Bearer $accessToken", "rmuhamed", 0).enqueue(object :
            Callback<BasicResponseDTO<List<AlbumDTO>>> {
            override fun onFailure(call: Call<BasicResponseDTO<List<AlbumDTO>>>, t: Throwable) {
                photosRetrievedLiveData.postValue(null)
            }

            override fun onResponse(
                call: Call<BasicResponseDTO<List<AlbumDTO>>>,
                response: Response<BasicResponseDTO<List<AlbumDTO>>>
            ) {
                response.body()?.let {
                    albumId = it.data!![0].id
                    getAlbumPictures(accessToken)
                } ?: run {
                    val error = JSONObject(response.errorBody()!!.string())
                    val message = error.optJSONObject("data")?.optString("error")
                    photosNotRetrievedLiveData.postValue(true)
                }
            }
        })
    }

    fun getAlbumPictures(accessToken: String) {
        RetrofitController.imgurAPI.picturesBy("Bearer $accessToken", albumId).enqueue(object :
            Callback<BasicResponseDTO<List<ImageDTO>>> {
            override fun onFailure(call: Call<BasicResponseDTO<List<ImageDTO>>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<BasicResponseDTO<List<ImageDTO>>>,
                response: Response<BasicResponseDTO<List<ImageDTO>>>
            ) {
                response.body()?.let {
                    photosRetrievedLiveData.postValue(response.body()!!.data)
                }
            }

        })
    }

    fun existsAnAlbum() = albumId.isNotBlank()

    fun createAlbum(accessToken: String) {
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