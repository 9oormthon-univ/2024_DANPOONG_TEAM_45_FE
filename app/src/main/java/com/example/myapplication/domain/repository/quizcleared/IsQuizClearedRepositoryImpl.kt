package com.example.myapplication.domain.repository.quizcleared

import com.example.myapplication.data.repository.remote.datasource.remote.IsQuizClearedDataSource
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.quizcleared.ClearStateListResponse
import com.example.myapplication.data.repository.remote.response.quizcleared.ClearStateResponse
import com.example.myapplication.repository.quizcleared.IsQuizClearedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class IsQuizClearedRepositoryImpl @Inject constructor(
    private val isQuizClearedDataSource: IsQuizClearedDataSource
) : IsQuizClearedRepository {
    override suspend fun postQuizCleared(quiz_id: Int): Flow<BaseResponse<Any>> =
        isQuizClearedDataSource.postQuizCleared(quiz_id)

    override suspend fun getQuizDistinct(isQuizCleared_id: Int) : Flow<BaseResponse<ClearStateResponse>> =
        isQuizClearedDataSource.getQuizDistinct(isQuizCleared_id)

    override suspend fun patchQuizSuccessState(
        isQuizCleared_id: Int,
        isCleared: Boolean
    ): Flow<BaseResponse<Any>> =
        isQuizClearedDataSource.patchQuizSuccessState(isQuizCleared_id, isCleared)

    override suspend fun getQuizAll(): Flow<BaseResponse<ClearStateListResponse>> =
        isQuizClearedDataSource.getQuizAll()

}
