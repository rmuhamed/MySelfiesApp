package com.rmuhamed.sample.myselfiesapp.api

import com.rmuhamed.sample.myselfiesapp.api.dto.*
import retrofit2.Call
import retrofit2.http.*

interface ImgurAPI {
    @POST("/oauth2/token")
    fun createToken(@Body tokenRequest: TokenRequestDTO): Call<TokenResponseDTO>

    @GET("/3/account/{userName}/verifyemail")
    fun verifyAccount(@Header("Authorization") authHeader: String, @Path("userName") userName: String)
            : Call<BasicResponseDTO<Boolean>>

    @GET("/3/album/{albumHash}/images")
    fun picturesBy(@Header("Authorization") authHeader: String, @Path("albumHash") albumHash: String)
            : Call<BasicResponseDTO<List<ImageDTO>>>

    @GET("/3/account/{username}/albums/{page}")
    fun albumsBy(
        @Header("Authorization") authHeader: String, @Path("username") userName: String, @Path(
            "page"
        ) page: Int
    ): Call<BasicResponseDTO<List<AlbumDTO>>>

    @POST("/3/album")
    fun createAlbum(@Header("Authorization") authHeader: String, @Body requestDTO: CreateAlbumRequestDTO)
            : Call<BasicResponseDTO<CreatedAlbumDTO>>

    @POST("/3/upload")
    fun uploadPhoto(@Header("Authorization") authHeader: String, @Body requestDTO: UploadImageRequestDTO)
            : Call<BasicResponseDTO<UploadedImageDTO>>
}