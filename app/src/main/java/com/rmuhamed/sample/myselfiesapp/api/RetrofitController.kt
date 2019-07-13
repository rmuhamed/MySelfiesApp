package com.rmuhamed.sample.myselfiesapp.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitController {
    lateinit var imgurAPI: ImgurAPI
    val BASE_URL = "https://api.imgur.com";

    fun get() {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY;

        val builder = OkHttpClient.Builder();
        builder.networkInterceptors().add(httpLoggingInterceptor);
        val client = builder.build();

        val gson = GsonBuilder()
            .setLenient()
            .create();

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        imgurAPI = retrofit.create(ImgurAPI::class.java)
    }
}