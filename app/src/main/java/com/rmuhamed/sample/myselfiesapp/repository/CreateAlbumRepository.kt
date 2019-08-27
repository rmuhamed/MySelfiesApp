package com.rmuhamed.sample.myselfiesapp.repository

import com.rmuhamed.sample.myselfiesapp.api.ImgurAPI
import com.rmuhamed.sample.myselfiesapp.api.dto.BasicResponseDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.CreateAlbumRequestDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.CreatedAlbumDTO
import com.rmuhamed.sample.myselfiesapp.cache.CacheDataSource
import com.rmuhamed.sample.myselfiesapp.cache.CacheDataSourceKeys
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateAlbumRepository(val api: ImgurAPI, private val cacheDataSource: CacheDataSource) :
    BasicRepository(cacheDataSource) {

    private var accessToken = getAuthenticatedCustomer().accessToken

    fun createAlbum(title: String, description: String, onError: (String) -> Unit, onSuccess: () -> Unit) {
        val requestDTO = CreateAlbumRequestDTO(title, description)
        api.createAlbum("Bearer $accessToken", requestDTO).enqueue(object :
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
                        cacheDataSource.save(CacheDataSourceKeys.ALBUM_ID, dto.id)
                        onSuccess.invoke()
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