package com.example.myapplication.data.repository.remote.request.quiz

data class QuizDto(
    val answer: String,
    val chapterId: Int,
    val level: Int,
    val question: String,
    val title: String,
    val type: String
)