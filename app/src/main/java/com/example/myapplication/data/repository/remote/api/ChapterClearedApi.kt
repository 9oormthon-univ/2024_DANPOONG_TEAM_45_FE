package com.example.myapplication.data.repository.remote.api

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.chaptercleared.ClearChapterStateListResponse
import com.example.myapplication.data.repository.remote.response.chaptercleared.ClearChapterStateResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ChapterClearedApi {

    //챕터 완료 여부 등록
    @POST("/v1/api/chapterclear")
    suspend fun postChapterCleared(
        @Query("chapterId") chapterId: Int
    ): BaseResponse<Any>

    //챕터 완료 여부 단 건 조회
    @GET("/v1/api/chapterclear/{isChapterCleared_id}")
    suspend fun getChapterDistinct(
        @Path("isChapterCleared_id") isChapterCleared_id: Int
    ): Response<ClearChapterStateResponse>

    //챕터 완료 여부 수정
    @PUT("/v1/api/chapterclear")
    suspend fun putChapterCleared(
        @Query("isChapterCleared_id") isChapterCleared_id: Int,
        @Query("isCleared") isCleared: Boolean
    ): Response<ResponseBody>

    //챕터 완료 여부 모두 조회
    @GET("/v1/api/chapterclear/all")
    suspend fun getChapterAll(
    ): Response<ClearChapterStateListResponse>

}