package com.rmuhamed.sample.myselfiesapp.api

import com.google.gson.GsonBuilder
import com.rmuhamed.sample.myselfiesapp.BuildConfig.API_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitController {
    var imgurAPI: ImgurAPI

    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY;

        val builder = OkHttpClient.Builder();
        builder.networkInterceptors().add(httpLoggingInterceptor);
        val client = builder.build();

        val gson = GsonBuilder()
            .setLenient()
            .create();

        val retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        imgurAPI = retrofit.create(ImgurAPI::class.java)
    }

}