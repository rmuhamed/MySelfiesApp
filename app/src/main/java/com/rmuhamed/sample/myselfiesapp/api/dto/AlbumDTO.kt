package com.rmuhamed.sample.myselfiesapp.api.dto

data class AlbumDTO(
    val account_id: Int,
    val account_url: String,
    val cover: String,
    val cover_edited: Int,
    val cover_height: Int,
    val cover_width: Int,
    val datetime: Int,
    val deletehash: String,
    val description: Any,
    val favorite: Boolean,
    val id: String,
    val images_count: Int,
    val in_gallery: Boolean,
    val include_album_ads: Boolean,
    val is_ad: Boolean,
    val is_album: Boolean,
    val layout: String,
    val link: String,
    val nsfw: Any,
    val order: Int,
    val privacy: String,
    val section: Any,
    val title: String,
    val views: Int
)