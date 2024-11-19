package com.example.myapplication.domain.repository.chapter

import com.example.codingland.data.repository.remote.request.chapter.RegisterChapterDto
import com.example.codingland.data.repository.remote.response.chapter.AllChapterResponse
import com.example.codingland.data.repository.remote.response.chapter.DistinctChapterResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response

interface ChapterRepository {
    suspend fun postCreateChapter(registerChapterDto: RegisterChapterDto): Flow<Response<ResponseBody>>
    suspend fun getDistinctChapter(
        chapter_id: Int,
        user_id: Int
    ): Flow<Response<DistinctChapterResponse>>

    suspend fun deleteChapter(lchapter_id: String): Flow<Response<ResponseBody>>
    suspend fun modifyChapter(registerChapterDto: RegisterChapterDto): Flow<Response<ResponseBody>>
    suspend fun getAllChapter(): Flow<Response<AllChapterResponse>>
}