package com.example.myapplication.presentation.ui.fragment.quest

import com.example.myapplication.data.repository.remote.response.chapter.QuizResponse

sealed class IslandDto {
    data class IslandRight(
        val id: Int,
        val isCleared: Boolean,
        val quizzes: List<QuizResponse>,
        val background: Int,
        val island: Int,
        val locked: Boolean,
        val name: String
    ) : IslandDto()

    data class IslandLeft(
        val id: Int,
        val isCleared: Boolean,
        val quizzes: List<QuizResponse>,
        val background: Int,
        val island: Int,
        val locked: Boolean,
        val name: String
    ) : IslandDto()
}