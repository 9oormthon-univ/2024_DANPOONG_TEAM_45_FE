package com.example.myapplication.data.repository.remote.response.chapter

data class QuizResponse(
    val isCleared: Boolean,
    val level: Int,
    val quizId: Int,
    val title: String,
    val type: String
)