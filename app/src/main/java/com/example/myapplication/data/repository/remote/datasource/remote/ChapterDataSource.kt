package com.example.myapplication.data.repository.remote.datasource.remote

import com.example.myapplication.data.repository.remote.request.chapter.RegisterChapterDto
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.chapter.AllChapterResponse
import com.example.myapplication.data.repository.remote.response.chapter.DistinctChapterResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response

interface ChapterDataSource {
    suspend fun postCreateChapter(registerChapterDto: RegisterChapterDto): Flow<BaseResponse<Any>>
    suspend fun getDistinctChapter(
        chapter_id: Int
    ): Flow<BaseResponse<DistinctChapterResponse>>

    suspend fun deleteChapter(lchapter_id: String): Flow<BaseResponse<Any>>
    suspend fun modifyChapter(registerChapterDto: RegisterChapterDto): Flow<Response<ResponseBody>>
    suspend fun getAllChapter(): Flow<BaseResponse<AllChapterResponse>>
}