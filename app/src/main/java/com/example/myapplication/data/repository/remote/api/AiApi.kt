package com.example.myapplication.data.repository.remote.api

import com.example.myapplication.data.repository.remote.request.ai.AiDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface AiApi {

    //AI 질문
    @POST("/ai")
    suspend fun postAi(
        @Body aiDTO: AiDTO
    )
}