package com.example.myapplication.data.repository.remote.api

import com.example.myapplication.data.repository.remote.request.chapter.RegisterChapterDto
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.chapter.AllChapterResponse
import com.example.myapplication.data.repository.remote.response.chapter.DistinctChapterResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChapterApi {

    //챕터 등록
    @POST("/v1/api/chapter")
    suspend fun postCreateChapter(
        @Body registerChapterDto: RegisterChapterDto
    ): BaseResponse<Any>

    //챕터 단 건 조회
    @GET("/v1/api/chapter/{chapter_id}")
    suspend fun getDistinctChapter(
        @Path("chapter_id") chapter_id : Int
    ): BaseResponse<DistinctChapterResponse>

    //챕터 삭제
    @DELETE("/v1/api/chapter/{chapter_id}")
    suspend fun deleteChapter(
        @Path("chapter_id") chapter_id : String
    ) : BaseResponse<Any>

    //챕터 수정
    @PATCH("/v1/api/chapter/{chapter_id}")
    suspend fun modifyChapter(
        @Body registerChapterDto: RegisterChapterDto
    ): Response<ResponseBody>

    //등록된 챕터 모두 조회
    @GET("/v1/api/chapter/all")
    suspend fun getAllChapter(
    ): BaseResponse<AllChapterResponse>

    //등록된 챕터 모두 조회
    @GET("/v1/api/chapter/{chapter_id}/reward/claim")
    suspend fun rewordSuccess(
        @Path("chapter_id") chapter_id : Int
    ): BaseResponse<Any>

}