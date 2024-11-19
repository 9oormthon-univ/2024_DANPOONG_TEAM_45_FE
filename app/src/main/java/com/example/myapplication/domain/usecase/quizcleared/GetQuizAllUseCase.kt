package com.example.myapplication.domain.usecase.quizcleared

import com.example.myapplication.data.repository.remote.response.quizcleared.ClearStateListResponse
import com.example.myapplication.domain.repository.quizcleared.IsQuizClearedRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class GetQuizAllUseCase @Inject constructor(
    private val quizClearedRepository: IsQuizClearedRepository
) {
    suspend operator fun invoke(
    ): Flow<Response<ClearStateListResponse>> = quizClearedRepository.getQuizAll()
}