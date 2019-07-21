package com.rmuhamed.sample.myselfiesapp.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmuhamed.sample.myselfiesapp.BuildConfig.*
import com.rmuhamed.sample.myselfiesapp.api.RetrofitController
import com.rmuhamed.sample.myselfiesapp.api.dto.BasicResponseDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.TokenRequestDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.TokenResponseDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    val loginAvailableLiveData  = MutableLiveData(false)
    val showProgressLiveData  = MutableLiveData(false)
    val loginSuccessfulLiveData = MutableLiveData(false)

    init {
        RetrofitController.get()
    }

    var userName = ""
        set(value) = value.run {
            field = value
            loginAvailableLiveData.value = this.isNotBlank()
        }

    fun verifyAccount() {
        showProgressLiveData.value = true

        RetrofitController.imgurAPI.createToken(
            TokenRequestDTO(API_REFRESH_TOKEN, API_CLIENT_ID, API_CLIENT_SECRET)
        ).enqueue(object : Callback<TokenResponseDTO> {
            override fun onFailure(call: Call<TokenResponseDTO>, t: Throwable) {
                showProgressLiveData.postValue(false)
                loginSuccessfulLiveData.postValue(false)
            }

            override fun onResponse(
                call: Call<TokenResponseDTO>,
                response: Response<TokenResponseDTO>
            ) {
                accountExists(response.body()?.access_token)
            }
        })
    }

    private fun accountExists(accessToken: String?) {
        accessToken?.let {
            val authorization = "Bearer $accessToken"

            RetrofitController.imgurAPI.verifyAccount(authorization, userName)
                .enqueue(object : Callback<BasicResponseDTO> {
                    override fun onFailure(call: Call<BasicResponseDTO>, t: Throwable) {
                        showProgressLiveData.postValue(false)
                        loginSuccessfulLiveData.postValue(false)
                    }

                    override fun onResponse(
                        call: Call<BasicResponseDTO>,
                        response: Response<BasicResponseDTO>
                    ) {
                        showProgressLiveData.postValue(false)
                        loginSuccessfulLiveData.postValue(response.body()?.success ?: false)
                    }

                })
        } ?: run {
            showProgressLiveData.postValue(false)
            loginSuccessfulLiveData.postValue(false)
        }

    }
}