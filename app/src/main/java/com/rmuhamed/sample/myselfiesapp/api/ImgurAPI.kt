package com.rmuhamed.sample.myselfiesapp.api

import com.rmuhamed.sample.myselfiesapp.api.dto.TokenRequestDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.TokenResponseDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ImgurAPI {
    @POST("/oauth2/token")
    fun createToken(@Body tokenRequest : TokenRequestDTO) : Call<TokenResponseDTO>

    @GET("/3/account/{username}")
    fun verifyAccount(@Path("userName") userName : String)

    @GET("/3/album/{albumHash}/images")
    fun getImages(@Path("albumHash") albumHash: String)
}