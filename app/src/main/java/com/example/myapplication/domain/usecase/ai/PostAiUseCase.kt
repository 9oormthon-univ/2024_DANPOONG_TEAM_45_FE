package com.example.myapplication.domain.usecase.ai

import com.example.myapplication.data.repository.remote.request.ai.AiDTO
import com.example.myapplication.domain.repository.ai.AiRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class PostAiUseCase @Inject constructor(
    private val aiRepository: AiRepository
) {
    suspend operator fun invoke(aiDTO: AiDTO): Flow<Response<ResponseBody>> = aiRepository.postAi(aiDTO)
}