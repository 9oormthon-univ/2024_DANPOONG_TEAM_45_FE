package com.example.myapplication.domain.repository.chaptercleared

import com.example.myapplication.data.repository.remote.datasource.remote.IsChapterClearedDataSource
import com.example.myapplication.data.repository.remote.response.chaptercleared.ClearChapterStateListResponse
import com.example.myapplication.data.repository.remote.response.chaptercleared.ClearChapterStateResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

data class ChapterClearedRepositoryImpl @Inject constructor(
    private val isChapterClearedDataSource: IsChapterClearedDataSource
) : ChapterClearedRepository {
    override suspend fun postChapterCleared(
        chapterId: Int,
        user_id: Int
    ): Flow<Response<ResponseBody>> =
        isChapterClearedDataSource.postChapterCleared(chapterId,user_id)

    override suspend fun getChapterDistinct(isChapterCleared_id: Int): Flow<Response<ClearChapterStateResponse>> =
        isChapterClearedDataSource.getChapterDistinct(isChapterCleared_id)

    override suspend fun putChapterCleared(
        isChapterCleared_id: Int,
        isCleared: Boolean
    ): Flow<Response<ResponseBody>> =
        isChapterClearedDataSource.putChapterCleared(isChapterCleared_id, isCleared)

    override suspend fun getChapterAll(): Flow<Response<ClearChapterStateListResponse>> =
        isChapterClearedDataSource.getChapterAll()

}
