package com.example.myapplication.data.repository.remote.api

import com.example.myapplication.data.repository.remote.request.home.PatchHomeDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.home.DistinctHomeIdResponse
import com.example.myapplication.data.repository.remote.response.home.HomeAllList
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface HomeApi {

    //홈 등록
    @POST("/home")
    suspend fun postHome(
    ): BaseResponse<Any>

    //홈 단 건 조회
    @GET("/home/{home_id}")
    suspend fun getDistinctChapter(
        @Path("home_id") home_id: Int
    ): DistinctHomeIdResponse

    //홈 삭제
    @DELETE("/home/{home_id}")
    suspend fun deleteHomeId(
        @Path("home_id") home_id: String
    ): BaseResponse<Any>

    //홈 수정
    @PATCH("/home/{home_id}")
    suspend fun patchHomeEdit(
        @Path("home_id") home_id: String,
        @Body patchHomeDTO: PatchHomeDTO
    ): BaseResponse<Any>

    //등록된 홈 모두 조회
    @GET("/home/all")
    suspend fun getAllHome(
    ): HomeAllList
}