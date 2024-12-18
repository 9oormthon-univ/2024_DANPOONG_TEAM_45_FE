package com.example.myapplication.data.repository.remote.datasourceImpl

import android.util.Log
import com.example.myapplication.data.repository.remote.api.QuizClearedApi
import com.example.myapplication.data.repository.remote.datasource.remote.IsQuizClearedDataSource
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.quizcleared.ClearStateListResponse
import com.example.myapplication.data.repository.remote.response.quizcleared.ClearStateResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class QuizClearedDataSorceImpl @Inject constructor(
    private val quizClearedApi: QuizClearedApi
) : IsQuizClearedDataSource {
    override suspend fun postQuizCleared(quiz_id: Int):Flow<BaseResponse<Any>> = flow {
        val result = quizClearedApi.postQuizCleared(quiz_id)
        emit(result)
    }.catch { e ->
        Log.e("getQuizDistinct 에러", e.message.toString())
    }


    override suspend fun getQuizDistinct(isQuizCleared_id: Int):  Flow<BaseResponse<ClearStateResponse>> = flow {
        val result = quizClearedApi.getQuizDistinct(isQuizCleared_id)
        emit(result)
    }.catch { e ->
        Log.e("getQuizDistinct 에러", e.message.toString())
    }

    override suspend fun patchQuizSuccessState(
        isQuizCleared_id: Int,
        isCleared: Boolean
    ): Flow<BaseResponse<Any>> = flow {
        val result = quizClearedApi.patchQuizSuccessState(isQuizCleared_id,isCleared)
        emit(result)
    }.catch { e ->
        Log.e("patchQuizSuccessState 에러", e.message.toString())
    }

    override suspend fun getQuizAll(): Flow<BaseResponse<ClearStateListResponse>> = flow {
        val result = quizClearedApi.getQuizAll()
        emit(result)
    }.catch { e ->
        Log.e("getQuizAll 에러", e.message.toString())
    }

}