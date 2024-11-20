package com.example.myapplication.data.repository.remote.api

import com.example.myapplication.data.repository.remote.request.quiz.EditQuizDto
import com.example.myapplication.data.repository.remote.request.quiz.QuizDto
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.quiz.AllQuizResponse
import com.example.myapplication.data.repository.remote.response.quiz.DistinctQuizResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface QuizApi {

    //퀴즈 생성
    @POST("/v1/api/quiz")
    suspend fun postCreateQuiz(
        @Body quizDto: QuizDto
    ): Response<ResponseBody>

    //퀴즈 수정
    @PATCH("/v1/api/quiz")
    suspend fun patchModifyQuiz(
        @Body editQuizDto: EditQuizDto
    ): Response<ResponseBody>

    //퀴즈 삭제
    @DELETE("/v1/api/quiz/{quiz_id}")
    suspend fun deleteQuiz(
        @Path("quiz_id") quiz_id: Int
    ): Response<ResponseBody>

    //퀴즈 단 건 조회
    @GET("/v1/api/quiz/{quiz_id}")
    suspend fun getDistinctQuiz(
        @Path("quiz_id") quiz_id: Int
    ): BaseResponse<DistinctQuizResponse>

    //모든 퀴즈 조회
    @GET("/v1/api/quiz/all")
    suspend fun getAllQuiz(
    ): BaseResponse<AllQuizResponse>

}