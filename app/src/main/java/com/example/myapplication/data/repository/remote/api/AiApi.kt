package com.example.myapplication.data.repository.remote.api

import com.example.myapplication.data.repository.remote.request.ai.AiDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AiApi {
    //AI 질문
    @GET("/v1/api/chat")
    suspend fun postAi(
        @Query("keyword") keyword : String
    ): BaseResponse<String>
}