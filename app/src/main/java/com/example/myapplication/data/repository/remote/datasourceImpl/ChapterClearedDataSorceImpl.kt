package com.example.myapplication.data.repository.remote.datasourceImpl

import android.util.Log
import com.example.myapplication.data.repository.remote.api.ChapterClearedApi
import com.example.myapplication.data.repository.remote.datasource.remote.IsChapterClearedDataSource
import com.example.myapplication.data.repository.remote.response.chaptercleared.ClearChapterStateListResponse
import com.example.myapplication.data.repository.remote.response.chaptercleared.ClearChapterStateResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class ChapterClearedDataSorceImpl @Inject constructor(
    private val chapterClearedApi: ChapterClearedApi
) : IsChapterClearedDataSource {

    override suspend fun postChapterCleared(
        chapterId: Int,
        user_id: Int
    ): Flow<Response<ResponseBody>> = flow {
        val result = chapterClearedApi.postChapterCleared(chapterId,user_id)
        emit(result)
    }.catch { e ->
        Log.e("getQuizDistinct 에러", e.message.toString())
    }

    override suspend fun getChapterDistinct(isChapterCleared_id: Int): Flow<Response<ClearChapterStateResponse>> = flow {
        val result = chapterClearedApi.getChapterDistinct(isChapterCleared_id)
        emit(result)
    }.catch { e ->
        Log.e("getChapterDistinct 에러", e.message.toString())
    }

    override suspend fun putChapterCleared(
        isChapterCleared_id: Int,
        isCleared: Boolean
    ): Flow<Response<ResponseBody>> = flow {
        val result = chapterClearedApi.putChapterCleared(isChapterCleared_id,isCleared)
        emit(result)
    }.catch { e ->
        Log.e("putChapterCleared 에러", e.message.toString())
    }

    override suspend fun getChapterAll(): Flow<Response<ClearChapterStateListResponse>> = flow {
        val result = chapterClearedApi.getChapterAll()
        emit(result)
    }.catch { e ->
        Log.e("getChapterAll 에러", e.message.toString())
    }

}