package com.example.myapplication.domain.usecase.quiz

import com.example.myapplication.domain.quiz.QuizRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class DeleteQuizUseCase @Inject constructor(
    private val quizRepository: QuizRepository
) {
    suspend operator fun invoke(quiz_id: Int
    ): Flow<Response<ResponseBody>> = quizRepository.deleteQuiz(quiz_id)
}