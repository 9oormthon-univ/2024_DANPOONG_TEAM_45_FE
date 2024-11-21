package com.example.myapplication.data.repository.remote.response.chapter

data class QuizResponse(
    val quizId: Int,
    val title: String,
    val level: Int,
    val isCleared: Boolean
)