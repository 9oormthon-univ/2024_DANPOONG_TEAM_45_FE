package com.example.myapplication.data.repository.remote.response.quiz

data class DailyCompleteResponse(
    val id : Int = 0,
    val userId : Int = 0,
    val date : String = "",
    val solvedCount : Int = 0,
    val isCompleted : Boolean = false
)
