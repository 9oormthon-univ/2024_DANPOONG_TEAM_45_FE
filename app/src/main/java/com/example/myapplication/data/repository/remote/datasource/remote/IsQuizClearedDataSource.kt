package com.example.myapplication.data.repository.remote.datasource.remote

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.quizcleared.ClearStateListResponse
import com.example.myapplication.data.repository.remote.response.quizcleared.ClearStateResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response

interface IsQuizClearedDataSource {
    suspend fun postQuizCleared(quiz_id: Int) : Flow<BaseResponse<Any>>
    suspend fun getQuizDistinct(isQuizCleared_id: Int) : Flow<BaseResponse<ClearStateResponse>>
    suspend fun patchQuizSuccessState(isQuizCleared_id: Int,isCleared : Boolean) : Flow<BaseResponse<Any>>
    suspend fun getQuizAll() : Flow<BaseResponse<ClearStateListResponse>>
}