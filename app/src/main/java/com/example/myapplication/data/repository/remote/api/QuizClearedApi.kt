package com.example.myapplication.data.repository.remote.api

import com.example.myapplication.data.repository.remote.response.quizcleared.ClearStateListResponse
import com.example.myapplication.data.repository.remote.response.quizcleared.ClearStateResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface QuizClearedApi {

    //풀이 완료 퀴즈 등록
    @POST("/isQuizCleared")
    suspend fun postQuizCleared(
        @Query("quiz_id") quiz_id: Int,
        @Query("user_id") user_id: Int
    ): Response<ResponseBody>

    //퀴즈 완료 여부 단 건 조회
    @GET("/isQuizCleared/{isQuizCleared_id}")
    suspend fun getQuizDistinct(
        @Path("isQuizCleared_id") isQuizCleared_id: Int
    ): Response<ClearStateResponse>

    //퀴즈 완료 여부 수정
    @PATCH("/isQuizCleared/{isQuizCleared_id}")
    suspend fun patchQuizSuccessState(
        @Path("isQuizCleared_id") isQuizCleared_id: Int,
        @Query("isCleared") isCleared : Boolean
    ): Response<ResponseBody>

    //퀴즈 완료 여부 모두 조회
    @GET("/isQuizCleared/all")
    suspend fun getQuizAll(
    ): Response<ClearStateListResponse>

}