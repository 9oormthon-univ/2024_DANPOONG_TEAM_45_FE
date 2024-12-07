package com.example.myapplication.domain.usecase.quiz

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.repository.quiz.QuizRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteQuizUseCase @Inject constructor(
    private val quizRepository: QuizRepository
) {
    suspend operator fun invoke(
        quiz_id: Int
    ):  Flow<BaseResponse<Any>> = quizRepository.deleteQuiz(quiz_id)
}