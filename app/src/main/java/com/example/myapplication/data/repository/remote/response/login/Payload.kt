package com.example.myapplication.data.repository.remote.response.login

data class Payload(
    val nickname : String = "",
    val picture: String = "",
    val existYn: Boolean = false,
    val accessToken: String = "",
    val refreshToken: String = ""
)