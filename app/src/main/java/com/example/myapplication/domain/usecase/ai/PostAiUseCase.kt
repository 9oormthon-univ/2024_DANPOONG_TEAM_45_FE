package com.example.myapplication.domain.usecase.ai

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.repository.ai.AiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostAiUseCase @Inject constructor(
    private val aiRepository: AiRepository
) {
    suspend operator fun invoke(aiDTO: String): Flow<BaseResponse<String>> = aiRepository.postAi(aiDTO)
}