package com.example.myapplication.domain.usecase.quiz

import com.example.myapplication.domain.quiz.QuizRepository
import com.example.myapplication.data.repository.remote.request.quiz.QuizDto
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class PostCreateQuizUseCase @Inject constructor(
    private val quizRepository: QuizRepository
) {
    suspend operator fun invoke(quizDto: QuizDto
    ): Flow<Response<ResponseBody>> = quizRepository.postCreateQuiz(quizDto)
}