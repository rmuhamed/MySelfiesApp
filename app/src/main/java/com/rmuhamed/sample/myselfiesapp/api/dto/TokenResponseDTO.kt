package com.rmuhamed.sample.myselfiesapp.api.dto

data class TokenResponseDTO(
    val access_token: String,
    val expires_In: String,
    val scope: String,
    val refresh_token: String,
    val account_id: String,
    val account_username: String
)
