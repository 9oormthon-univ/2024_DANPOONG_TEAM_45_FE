package com.example.myapplication.domain.repository.ai

import com.example.myapplication.data.repository.remote.request.ai.AiDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response

interface AiRepository {
    suspend fun postAi(keyword : String): Flow<BaseResponse<String>>

}