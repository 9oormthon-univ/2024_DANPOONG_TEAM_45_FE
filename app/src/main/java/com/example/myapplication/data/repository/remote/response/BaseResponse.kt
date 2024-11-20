package com.example.myapplication.data.repository.remote.response

data class BaseResponse<T>(
    val result: Result = Result(),
    val payload: T? = null
)

// Result 클래스
data class Result(
    val code: Int = 0,
    val message: String = ""
)