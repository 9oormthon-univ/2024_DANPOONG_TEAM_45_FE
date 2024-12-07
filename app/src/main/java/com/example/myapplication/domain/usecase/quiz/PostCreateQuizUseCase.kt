package com.example.myapplication.domain.usecase.quiz

import com.example.myapplication.data.repository.remote.request.quiz.QuizDto
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.repository.quiz.QuizRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostCreateQuizUseCase @Inject constructor(
    private val quizRepository: QuizRepository
) {
    suspend operator fun invoke(quizDto: QuizDto
    ):Flow<BaseResponse<Any>> = quizRepository.postCreateQuiz(quizDto)
}