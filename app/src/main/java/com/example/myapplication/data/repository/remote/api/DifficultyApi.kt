package com.example.myapplication.data.repository.remote.api

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.difficulty.DifficultyLevelList
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

//관리자용
interface DifficultyApi {

    //난이도 등록
    @POST("/v1/api/difficulty")
    suspend fun postDifficultyLevel(
        @Query("level") level: String
    ): BaseResponse<Any>

    //난이도 삭제
    @DELETE("/v1/api/difficulty")
    suspend fun deleteDifficultyLevel(
        @Query("difficulty_id") difficulty_id: String
    ): BaseResponse<Any>

    //난이도 수정
    @PATCH("/difficulty")
    suspend fun patchDifficultyLevel(
        @Query("difficulty_id") difficulty_id: String,
        @Query("level") level: Int
    ): Response<ResponseBody>

    //등록된 난이도 모두 조회
    @POST("/difficulty/all")
    suspend fun getDifficultyAllLevel(
    ): Response<DifficultyLevelList>

}