package com.example.myapplication.data.repository.remote.response.chapter

data class DistinctChapterResponse(
    val id: Int = 0,
    val name: String = "",
    val isCleared: Boolean = false,
    val quizzes: List<QuizResponse> = listOf(),
    val isRewardButtonActive : Boolean = true
)