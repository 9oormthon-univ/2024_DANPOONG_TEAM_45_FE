package com.example.myapplication.domain.quizcleared

import com.example.myapplication.data.repository.remote.response.quizcleared.ClearStateListResponse
import com.example.myapplication.data.repository.remote.response.quizcleared.ClearStateResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response

interface IsQuizClearedRepository {
    suspend fun postQuizCleared(quiz_id: Int,user_id: Int) : Flow<Response<ResponseBody>>
    suspend fun getQuizDistinct(isQuizCleared_id: Int) : Flow<Response<ClearStateResponse>>
    suspend fun patchQuizSuccessState(isQuizCleared_id: Int,isCleared : Boolean) : Flow<Response<ResponseBody>>
    suspend fun getQuizAll() : Flow<Response<ClearStateListResponse>>
}