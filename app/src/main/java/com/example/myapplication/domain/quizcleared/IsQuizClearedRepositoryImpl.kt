package com.example.myapplication.domain.quizcleared

import com.example.myapplication.data.repository.remote.datasource.remote.IsQuizClearedDataSource
import com.example.myapplication.data.repository.remote.response.quizcleared.ClearStateListResponse
import com.example.myapplication.data.repository.remote.response.quizcleared.ClearStateResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

data class IsQuizClearedRepositoryImpl @Inject constructor(
    private val isQuizClearedDataSource: IsQuizClearedDataSource
) : IsQuizClearedRepository {
    override suspend fun postQuizCleared(quiz_id: Int, user_id: Int): Flow<Response<ResponseBody>> =
        isQuizClearedDataSource.postQuizCleared(quiz_id,user_id)

    override suspend fun getQuizDistinct(isQuizCleared_id: Int): Flow<Response<ClearStateResponse>> =
        isQuizClearedDataSource.getQuizDistinct(isQuizCleared_id)

    override suspend fun patchQuizSuccessState(
        isQuizCleared_id: Int,
        isCleared: Boolean
    ): Flow<Response<ResponseBody>> =
        isQuizClearedDataSource.patchQuizSuccessState(isQuizCleared_id,isCleared)

    override suspend fun getQuizAll(): Flow<Response<ClearStateListResponse>> =
        isQuizClearedDataSource.getQuizAll()

}
