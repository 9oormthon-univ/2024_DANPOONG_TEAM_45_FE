package com.example.myapplication.repository.chapter

import com.example.myapplication.data.repository.remote.request.chapter.RegisterChapterDto
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.chapter.AllChapterResponse
import com.example.myapplication.data.repository.remote.response.chapter.DistinctChapterResponse
import kotlinx.coroutines.flow.Flow

interface ChapterRepository {
    suspend fun postCreateChapter(registerChapterDto: RegisterChapterDto):  Flow<BaseResponse<Any>>
    suspend fun getDistinctChapter(chapter_id: Int): Flow<BaseResponse<DistinctChapterResponse>>
    suspend fun deleteChapter(lchapter_id: String): Flow<BaseResponse<Any>>
    suspend fun modifyChapter(chapter_id: String, registerChapterDto: RegisterChapterDto): Flow<BaseResponse<Any>>
    suspend fun getAllChapter(): Flow<BaseResponse<AllChapterResponse>>
    suspend fun rewardSuccess(chapter_id : Int): Flow<BaseResponse<Any>>
}