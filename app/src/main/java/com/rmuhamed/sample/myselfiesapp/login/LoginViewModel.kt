package com.rmuhamed.sample.myselfiesapp.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rmuhamed.sample.myselfiesapp.api.RetrofitController
import com.rmuhamed.sample.myselfiesapp.api.dto.TokenRequestDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.TokenResponseDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    val loginAvailableLiveData  = MutableLiveData(false)
    val showProgressLiveData  = MutableLiveData(false)
    val loginSuccessfulLiveData = MutableLiveData(false)

    var userName = ""
        set(value) = value.run {
            loginAvailableLiveData.value = this.isNotBlank()
        }

    fun verifyAccount() {
        showProgressLiveData.value = true

        RetrofitController.get()
        RetrofitController.imgurAPI.createToken(
            TokenRequestDTO(
                "760196043a2599811a13b01d838a60eed790332b",
                "20e4ce28b9818fb",
                "f7a5a634b505d6ce71313a704119fa3b609cff13"
            )
        ).enqueue(object : Callback<TokenResponseDTO> {
            override fun onFailure(call: Call<TokenResponseDTO>, t: Throwable) {
                showProgressLiveData.postValue(false)
                loginSuccessfulLiveData.postValue(false)
            }

            override fun onResponse(
                call: Call<TokenResponseDTO>,
                response: Response<TokenResponseDTO>
            ) {
                showProgressLiveData.postValue(false)
                loginSuccessfulLiveData.postValue(true)
            }
        })
    }
}