package com.example.myapplication.data.repository.remote.response

data class BaseResponse<T>(
    val status : String,
    val isSuccess : String,
    val message : String = "",
    val result: T? = null
)
