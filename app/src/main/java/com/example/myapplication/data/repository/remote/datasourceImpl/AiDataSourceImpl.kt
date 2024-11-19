package com.example.myapplication.data.repository.remote.datasourceImpl

import android.util.Log
import com.example.myapplication.data.repository.remote.api.AiApi
import com.example.myapplication.data.repository.remote.api.ChapterClearedApi
import com.example.myapplication.data.repository.remote.datasource.remote.AiDataSource
import com.example.myapplication.data.repository.remote.datasource.remote.IsChapterClearedDataSource
import com.example.myapplication.data.repository.remote.request.ai.AiDTO
import com.example.myapplication.data.repository.remote.response.chaptercleared.ClearChapterStateListResponse
import com.example.myapplication.data.repository.remote.response.chaptercleared.ClearChapterStateResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class AiDataSourceImpl @Inject constructor(
    private val aiApi: AiApi
) : AiDataSource {

    override suspend fun postAi(aiDTO: AiDTO): Flow<Response<ResponseBody>> = flow {
        val result = aiApi.postAi(aiDTO)
        emit(result)
    }.catch { e ->
        Log.e("getChapterAll 에러", e.message.toString())
    }
}