package com.example.myapplication.data.repository.remote.datasource.remote

import com.example.myapplication.data.repository.remote.request.home.PatchHomeDTO
import com.example.myapplication.data.repository.remote.response.home.DistinctHomeIdResponse
import com.example.myapplication.data.repository.remote.response.home.HomeAllList
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response

interface HomeDataSource {
    //홈 등록
    suspend fun postHome(): Flow<Response<ResponseBody>>

    //홈 단 건 조회
    suspend fun getDistinctChapter(home_id: Int): Flow<DistinctHomeIdResponse>

    //홈 삭제
    suspend fun deleteHomeId(home_id: String): Flow<Response<ResponseBody>>

    //홈 수정
    suspend fun patchHomeEdit(
        home_id: String,
        patchHomeDTO: PatchHomeDTO
    ): Flow<Response<ResponseBody>>

    //등록된 홈 모두 조회
    suspend fun getAllHome(): Flow<HomeAllList>

}