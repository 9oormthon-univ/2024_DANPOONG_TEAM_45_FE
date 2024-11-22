package com.example.myapplication.domain.repository.chaptercleared

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.chaptercleared.ClearChapterStateListResponse
import com.example.myapplication.data.repository.remote.response.chaptercleared.ClearChapterStateResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response

interface ChapterClearedRepository {
    suspend fun postChapterCleared(chapterId: Int): Flow<BaseResponse<Any>>
    suspend fun getChapterDistinct(isChapterCleared_id: Int): Flow<Response<ClearChapterStateResponse>>
    suspend fun putChapterCleared(
        isChapterCleared_id: Int,
        isCleared: Boolean
    ): Flow<Response<ResponseBody>>

    suspend fun getChapterAll(): Flow<Response<ClearChapterStateListResponse>>
}