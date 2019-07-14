package com.rmuhamed.sample.myselfiesapp.api.dto

data class TokenRequestDTO(
    val refresh_token: String,
    val client_id: String,
    val client_secret: String,
    val grant_type: String = "refresh_token"
)
