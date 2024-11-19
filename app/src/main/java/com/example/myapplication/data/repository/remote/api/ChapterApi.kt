package com.example.myapplication.data.repository.remote.api

import com.example.myapplication.data.repository.remote.request.chapter.RegisterChapterDto
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
    @POST("/chapter")
    suspend fun postCreateChapter(
        @Body registerChapterDto: RegisterChapterDto
    ): Response<ResponseBody>

    //챕터 단 건 조회
    @GET("/chapter/{chapter_id}")
    suspend fun getDistinctChapter(
        @Path("chapter_id") chapter_id : Int,
        @Query("user_id") user_id : Int
    ): Response<DistinctChapterResponse>

    //챕터 삭제
    @DELETE("/chapter/{chapter_id}")
    suspend fun deleteChapter(
        @Path("chapter_id") chapter_id : String
    ) : Response<ResponseBody>

    //챕터 수정
    @PATCH("/chapter/{chapter_id}")
    suspend fun modifyChapter(
        @Body registerChapterDto: RegisterChapterDto
    ): Response<ResponseBody>

    //등록된 챕터 모두 조회
    @GET("/chapter/all")
    suspend fun getAllChapter(
    ): Response<AllChapterResponse>

}