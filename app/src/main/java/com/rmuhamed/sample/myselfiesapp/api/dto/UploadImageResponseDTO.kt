package com.rmuhamed.sample.myselfiesapp.api.dto

data class UploadImageResponseDTO(
    val `data`: ImageDataResponseDTO,
    val status: Int,
    val success: Boolean
)