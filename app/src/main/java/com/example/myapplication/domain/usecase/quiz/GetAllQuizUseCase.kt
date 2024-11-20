package com.example.myapplication.domain.usecase.quiz

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.quiz.AllQuizResponse
import com.example.myapplication.domain.repository.quiz.QuizRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class GetAllQuizUseCase @Inject constructor(
    private val quizRepository: QuizRepository
) {
    suspend operator fun invoke(
    ): Flow<BaseResponse<AllQuizResponse>> = quizRepository.getQuizAll()
}