package com.example.myapplication.domain.usecase.quiz

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.quiz.DailyCompleteResponse
import com.example.myapplication.repository.quiz.QuizRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostCompleteQuizUseCase @Inject constructor(
    private val quizRepository: QuizRepository
) {
    suspend operator fun invoke(
    ):  Flow<BaseResponse<Any>> = quizRepository.postCompleteQuest()
}