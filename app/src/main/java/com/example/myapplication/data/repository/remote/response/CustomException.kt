package com.example.myapplication.data.repository.remote.response

class CustomException(
    val code: Int,       // 예외 코드
    override val message: String // 예외 메시지
) : Exception(message)
