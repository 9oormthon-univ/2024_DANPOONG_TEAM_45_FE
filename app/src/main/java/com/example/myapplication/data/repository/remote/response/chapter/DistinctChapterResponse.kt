package com.example.myapplication.data.repository.remote.response.chapter

data class DistinctChapterResponse(
    val id: Int,
    val name: String,
    val isCleared: Boolean,
    val quizzes: Quizzes
)