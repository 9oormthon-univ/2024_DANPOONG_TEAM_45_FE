package com.example.myapplication.domain.usecase.quizcleared

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.repository.quizcleared.IsQuizClearedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostQuizClearedUseCase @Inject constructor(
    private val quizClearedRepository: IsQuizClearedRepository
) {
    suspend operator fun invoke( quiz_id: Int
    ): Flow<BaseResponse<Any>> = quizClearedRepository.postQuizCleared(quiz_id)
}