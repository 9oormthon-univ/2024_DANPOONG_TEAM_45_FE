package com.example.myapplication.repository.ai

import com.example.myapplication.data.repository.remote.response.BaseResponse
import kotlinx.coroutines.flow.Flow

interface AiRepository {
    suspend fun postAi(keyword : String): Flow<BaseResponse<String>>

}