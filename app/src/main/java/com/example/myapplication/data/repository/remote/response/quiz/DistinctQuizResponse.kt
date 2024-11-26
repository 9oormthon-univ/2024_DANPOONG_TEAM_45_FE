package com.example.myapplication.data.repository.remote.response.quiz

data class DistinctQuizResponse(
    val quizId : Int = 0,
    val questions: List<Question> = listOf(),
    val answers : List<Answer> = listOf(),
    val type: String = "",
    val title : String = "",
    val message : String = "",
    val chapterId : Int = 0,
    val level : Int = 0,
    val isCleared : Boolean = false,
    val hint : String = ""
)

data class Question(
    val type : String = "",
    val msg : String = "",
    val repeat : Int = 0
)

data class Answer(
    val type : String = "",
    val msg : String = "",
    val repeat : Int = 0
)