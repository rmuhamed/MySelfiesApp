package com.rmuhamed.sample.myselfiesapp.repository

import com.rmuhamed.sample.myselfiesapp.api.RetrofitController
import com.rmuhamed.sample.myselfiesapp.api.dto.BasicResponseDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.UploadImageRequestDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.UploadedImageDTO
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CameraRepository(private val albumId: String? = "albumId", private val accessToken: String? = "accessToken") {

    init {
        RetrofitController.get()
    }

    fun upload(
        name: String,
        title: String,
        description: String,
        base64: String,
        onError: (String) -> Unit,
        onSuccess: (UploadedImageDTO) -> Unit
    ) {
        val dto = UploadImageRequestDTO(
            base64,
            albumId ?: "",
            "image",
            name,
            title,
            description
        )

        RetrofitController.imgurAPI.uploadPhoto("Bearer $accessToken", dto).enqueue(object :
            Callback<BasicResponseDTO<UploadedImageDTO>> {
            override fun onFailure(call: Call<BasicResponseDTO<UploadedImageDTO>>, t: Throwable) {
                onError.invoke(t.localizedMessage ?: "Unknown error")
            }

            override fun onResponse(
                call: Call<BasicResponseDTO<UploadedImageDTO>>,
                response: Response<BasicResponseDTO<UploadedImageDTO>>
            ) {
                response.body()?.let {
                    onSuccess.invoke(it.data!!)
                } ?: run {
                    val error = JSONObject(response.errorBody()!!.string())
                    val message = error.optJSONObject("data")?.optString("error")
                    onError.invoke(message ?: "Image couldn't be uploaded")
                }
            }
        })
    }
}