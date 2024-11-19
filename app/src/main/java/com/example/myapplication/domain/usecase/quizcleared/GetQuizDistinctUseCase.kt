package com.example.myapplication.domain.usecase.quizcleared

import com.example.myapplication.data.repository.remote.response.quizcleared.ClearStateResponse
import com.example.myapplication.domain.repository.quizcleared.IsQuizClearedRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class GetQuizDistinctUseCase @Inject constructor(
    private val quizClearedRepository: IsQuizClearedRepository
) {
    suspend operator fun invoke(
        isQuizCleared_id: Int
    ): Flow<Response<ClearStateResponse>> = quizClearedRepository.getQuizDistinct(isQuizCleared_id)
}