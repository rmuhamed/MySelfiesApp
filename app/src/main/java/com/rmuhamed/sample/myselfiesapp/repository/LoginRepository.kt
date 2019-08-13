package com.rmuhamed.sample.myselfiesapp.repository

import com.rmuhamed.sample.myselfiesapp.BuildConfig
import com.rmuhamed.sample.myselfiesapp.api.ImgurAPI
import com.rmuhamed.sample.myselfiesapp.api.dto.BasicResponseDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.TokenRequestDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.TokenResponseDTO
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository(val api: ImgurAPI) {
    private var accessToken: String = ""

    fun createToken(onError: (String) -> Unit, onSuccess: (Boolean) -> Unit) {
        val requestDTO =
            TokenRequestDTO(
                BuildConfig.API_REFRESH_TOKEN,
                BuildConfig.API_CLIENT_ID,
                BuildConfig.API_CLIENT_SECRET
            )

        api.createToken(requestDTO).enqueue(object : Callback<TokenResponseDTO> {
            override fun onFailure(call: Call<TokenResponseDTO>, t: Throwable) {
                onError.invoke(t.localizedMessage ?: "Unknown error")
            }

            override fun onResponse(call: Call<TokenResponseDTO>, response: Response<TokenResponseDTO>) {
                accessToken = response.body()?.access_token ?: ""
                onSuccess.invoke(accessToken.isNotBlank())
            }
        })
    }

    fun accountExists(userName: String, onError: (String) -> Unit, onSuccess: (String) -> Unit) {
        val authorizationHeader = "Bearer $accessToken"

        api.verifyAccount(authorizationHeader, userName)
            .enqueue(object : Callback<BasicResponseDTO<Boolean>> {
                override fun onFailure(call: Call<BasicResponseDTO<Boolean>>, t: Throwable) {
                    onError.invoke(t.localizedMessage ?: "Unknown error")
                }

                override fun onResponse(
                    call: Call<BasicResponseDTO<Boolean>>,
                    response: Response<BasicResponseDTO<Boolean>>
                ) {
                    response.body()?.let {
                        if (it.success) {
                            onSuccess.invoke(accessToken)
                        } else {
                            onSuccess.invoke("")
                        }
                    } ?: run {
                        val error = JSONObject(response.errorBody()!!.string())
                        val message = error.optJSONObject("data")?.optString("error") ?: ""

                        onError.invoke(message)
                    }
                }
            })
    }
}
