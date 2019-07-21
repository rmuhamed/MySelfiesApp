package com.rmuhamed.sample.myselfiesapp.api

import com.rmuhamed.sample.myselfiesapp.api.dto.BasicResponseDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.TokenRequestDTO
import com.rmuhamed.sample.myselfiesapp.api.dto.TokenResponseDTO
import retrofit2.Call
import retrofit2.http.*

interface ImgurAPI {
    @POST("/oauth2/token")
    fun createToken(@Body tokenRequest : TokenRequestDTO) : Call<TokenResponseDTO>

    @GET("/3/account/{userName}/verifyemail")
    fun verifyAccount(@Header("Authorization") authHeader: String, @Path("userName") userName: String): Call<BasicResponseDTO>

    @GET("/3/album/{albumHash}/images")
    fun getImages(@Path("albumHash") albumHash: String)
}