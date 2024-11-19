package com.example.myapplication.data.repository.remote.request.quiz

data class EditQuizDto(
    val answer: String,
    val chapterId: Int,
    val level: Int,
    val question: String,
    val quizId: Int,
    val title: String,
    val type: String
)