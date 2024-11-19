package com.example.myapplication.data.repository.remote.response.quiz

data class DistinctQuizResponse(
    val answer: String,
    val chapterId: Int,
    val isCleared: Boolean,
    val level: Int,
    val question: String,
    val quizId: Int,
    val title: String,
    val type: String
)