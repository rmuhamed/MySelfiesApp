package com.rmuhamed.sample.myselfiesapp.api.dto

data class ImageDTO(
    val animated: Boolean,
    val bandwidth: Int,
    val datetime: Int,
    val deletehash: String,
    val description: Any,
    val height: Int,
    val id: String,
    val link: String,
    val section: Any,
    val size: Int,
    val title: Any,
    val type: String,
    val views: Int,
    val width: Int
)