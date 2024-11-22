package com.example.myapplication.data.repository.remote.request.quiz

import com.example.myapplication.data.repository.remote.response.quiz.Answer
import com.example.myapplication.data.repository.remote.response.quiz.Question

data class QuizDto(
    val questions: List<Question> = listOf(),
    val answers : List<Answer> = listOf(),
    val type: String = "",
    val title : String = "",
    val message : String = "",
    val chapterId : Int = 0,
    val level : Int = 0,
    val hint : String = ""
)