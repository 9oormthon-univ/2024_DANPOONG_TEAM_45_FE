package com.example.myapplication.data.repository.remote.response.quizcleared

data class ClearStateResponse(
    val id : Int,
    val userId : Int,
    val quizId : Int,
    val isCleared : Boolean
)
