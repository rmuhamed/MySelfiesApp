package com.rmuhamed.sample.myselfiesapp.api.dto

data class BasicResponseDTO<T>(
    val data: T? = null,
    val status: Int = 0,
    val success: Boolean
)