package com.example.myapplication.data.repository.remote.request.login

data class UserDTO(
    val userId : Int = 0,
    val subId : String = "",
    val name : String = "",
    val picture : String = "",
    val email : String = ""
)
