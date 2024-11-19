package com.example.myapplication.domain.usecase.quiz

import com.example.myapplication.data.repository.remote.response.quiz.DistinctQuizResponse
import com.example.myapplication.domain.repository.quiz.QuizRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class GetDistinctQuizUseCase @Inject constructor(
    private val quizRepository: QuizRepository
) {
    suspend operator fun invoke(quiz_id: Int,user_id: Int
    ): Flow<Response<DistinctQuizResponse>> = quizRepository.getDistinctQuiz(quiz_id,user_id)
}