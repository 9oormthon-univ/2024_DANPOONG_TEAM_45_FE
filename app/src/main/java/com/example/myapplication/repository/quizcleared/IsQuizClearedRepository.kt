package com.example.myapplication.repository.quizcleared

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.quizcleared.ClearStateListResponse
import com.example.myapplication.data.repository.remote.response.quizcleared.ClearStateResponse
import kotlinx.coroutines.flow.Flow

interface IsQuizClearedRepository {
    suspend fun postQuizCleared(quiz_id: Int): Flow<BaseResponse<Any>>
    suspend fun getQuizDistinct(isQuizCleared_id: Int): Flow<BaseResponse<ClearStateResponse>>
    suspend fun patchQuizSuccessState(
        isQuizCleared_id: Int,
        isCleared: Boolean
    ): Flow<BaseResponse<Any>>

    suspend fun getQuizAll(): Flow<BaseResponse<ClearStateListResponse>>
}