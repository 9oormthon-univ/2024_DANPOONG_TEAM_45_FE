package com.example.myapplication.data.repository.remote.datasource.remote

import com.example.myapplication.data.repository.remote.request.ai.AiDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface AiDataSource {
    //AI 질문
    suspend fun postAi(keyword : String): Flow<BaseResponse<String>>
}