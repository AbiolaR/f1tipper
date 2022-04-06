package com.keplerworks.f1tipper.dto

data class LoginDataDTO(
    val username: String,
    val access_token: String,
    val refresh_token: String,
    val roles: List<String>
)
