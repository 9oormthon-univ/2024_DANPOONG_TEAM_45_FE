package com.example.myapplication.domain.usecase.quizcleared

import com.example.myapplication.domain.quizcleared.IsQuizClearedRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class PostQuizClearedUseCase @Inject constructor(
    private val quizClearedRepository: IsQuizClearedRepository
) {
    suspend operator fun invoke( quiz_id: Int,user_id: Int
    ): Flow<Response<ResponseBody>> = quizClearedRepository.postQuizCleared(quiz_id, user_id)
}