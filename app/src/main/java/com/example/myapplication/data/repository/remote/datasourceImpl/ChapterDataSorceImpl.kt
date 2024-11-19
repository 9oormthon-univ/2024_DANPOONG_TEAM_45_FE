package com.example.myapplication.data.repository.remote.datasourceImpl

import android.util.Log
import com.example.myapplication.data.repository.remote.api.ChapterApi
import com.example.myapplication.data.repository.remote.datasource.remote.ChapterDataSource
import com.example.myapplication.data.repository.remote.request.chapter.RegisterChapterDto
import com.example.myapplication.data.repository.remote.response.chapter.AllChapterResponse
import com.example.myapplication.data.repository.remote.response.chapter.DistinctChapterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class ChapterDataSorceImpl @Inject constructor(
    private val chapterApi: ChapterApi
) : ChapterDataSource {
    override suspend fun postCreateChapter(registerChapterDto: RegisterChapterDto): Flow<Response<ResponseBody>>  = flow {
        val result = chapterApi.postCreateChapter(registerChapterDto)
        emit(result)
    }.catch { e ->
        Log.e("postCreateChapter 에러", e.message.toString())
    }

    override suspend fun getDistinctChapter(
        chapter_id: Int,
        user_id: Int
    ): Flow<Response<DistinctChapterResponse>> = flow {
        val result = chapterApi.getDistinctChapter(chapter_id, user_id)
        emit(result)
    }.catch { e ->
        Log.e("getDistinctChapter 에러", e.message.toString())
    }

    override suspend fun deleteChapter(lchapter_id: String): Flow<Response<ResponseBody>> = flow {
        val result = chapterApi.deleteChapter(lchapter_id)
        emit(result)
    }.catch { e ->
        Log.e("deleteChapter 에러", e.message.toString())
    }

    override suspend fun modifyChapter(registerChapterDto: RegisterChapterDto): Flow<Response<ResponseBody>> = flow {
        val result = chapterApi.modifyChapter(registerChapterDto)
        emit(result)
    }.catch { e ->
        Log.e("modifyChapter 에러", e.message.toString())
    }

    override suspend fun getAllChapter(): Flow<Response<AllChapterResponse>> = flow {
        val result = chapterApi.getAllChapter()
        emit(result)
    }.catch { e ->
        Log.e("getAllChapter 에러", e.message.toString())
    }

}