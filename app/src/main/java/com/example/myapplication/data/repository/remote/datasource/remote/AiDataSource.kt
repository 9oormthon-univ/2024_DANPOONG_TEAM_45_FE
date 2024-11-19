package com.example.myapplication.data.repository.remote.datasource.remote

import com.example.myapplication.data.repository.remote.request.ai.AiDTO
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response

interface AiDataSource {
    //AI 질문
    suspend fun postAi(aiDTO: AiDTO): Flow<Response<ResponseBody>>
}