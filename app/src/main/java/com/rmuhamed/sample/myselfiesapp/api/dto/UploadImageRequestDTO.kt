package com.rmuhamed.sample.myselfiesapp.api.dto

data class UploadImageRequestDTO(
    val image: String,
    val type: String,
    val name: String,
    val title: String,
    val description: String
)