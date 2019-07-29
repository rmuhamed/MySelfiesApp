package com.rmuhamed.sample.myselfiesapp.login

import android.util.Log
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
    val loginAvailableLiveData = MutableLiveData(false)
    val loginInProgressLiveData = MutableLiveData(false)
    val loginSuccessfulLiveData = MutableLiveData("")
    val credentialsInvalidLiveData = MutableLiveData(false)

    init {
        RetrofitController.get()
    }

    var userName = ""
        set(value) = value.run {
            field = value
            loginAvailableLiveData.value = this.isNotBlank()
        }

    fun verifyAccount() {
        if (userName.isNotBlank()) {
            loginInProgressLiveData.value = true

            RetrofitController.imgurAPI.createToken(
                TokenRequestDTO(API_REFRESH_TOKEN, API_CLIENT_ID, API_CLIENT_SECRET)
            ).enqueue(object : Callback<TokenResponseDTO> {
                override fun onFailure(call: Call<TokenResponseDTO>, t: Throwable) {
                    Log.e(LoginViewModel::class.java.simpleName, t.localizedMessage ?: "Error")
                    onErrorToBeNotified()
                }

                override fun onResponse(
                    call: Call<TokenResponseDTO>,
                    response: Response<TokenResponseDTO>
                ) {
                    accountExists(response.body()?.access_token)
                }
            })
        } else {
            loginAvailableLiveData.value = false
        }
    }

    private fun accountExists(accessToken: String?) = accessToken?.let {
        val authorization = "Bearer $accessToken"

        RetrofitController.imgurAPI.verifyAccount(authorization, userName)
            .enqueue(object : Callback<BasicResponseDTO<Boolean>> {
                override fun onFailure(call: Call<BasicResponseDTO<Boolean>>, t: Throwable) {
                    Log.e(LoginViewModel::class.java.simpleName, t.localizedMessage ?: "Error")
                    onErrorToBeNotified()
                }

                override fun onResponse(
                    call: Call<BasicResponseDTO<Boolean>>,
                    response: Response<BasicResponseDTO<Boolean>>
                ) {
                    response.body()?.let {
                        loginInProgressLiveData.postValue(false)
                        loginSuccessfulLiveData.postValue(accessToken)
                    } ?: run {
                        onErrorToBeNotified()
                    }
                }

            })
    } ?: run {
        onErrorToBeNotified()
    }

    private fun onErrorToBeNotified() {
        loginInProgressLiveData.postValue(false)
        credentialsInvalidLiveData.postValue(true)
    }
}