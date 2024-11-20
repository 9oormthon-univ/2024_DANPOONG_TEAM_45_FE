package com.example.myapplication.domain.repository.ai

import com.example.myapplication.data.repository.remote.datasource.remote.AiDataSource
import com.example.myapplication.data.repository.remote.request.ai.AiDTO
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class AiRepositoryImpl @Inject constructor(
    private val aiDataSource: AiDataSource
) : AiRepository{
    override suspend fun postAi(aiDTO: AiDTO): Flow<Response<ResponseBody>> = aiDataSource.postAi(aiDTO)
}