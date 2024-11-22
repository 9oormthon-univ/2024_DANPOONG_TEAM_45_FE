package com.example.myapplication.data.repository.remote.datasourceImpl

import android.util.Log
import com.example.myapplication.data.repository.remote.api.QuizApi
import com.example.myapplication.data.repository.remote.datasource.remote.QuizDataSource
import com.example.myapplication.data.repository.remote.request.quiz.EditQuizDto
import com.example.myapplication.data.repository.remote.request.quiz.QuizDto
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.quiz.AllQuizResponse
import com.example.myapplication.data.repository.remote.response.quiz.DistinctQuizResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class QuizDataSorceImpl @Inject constructor(
    private val quizApi: QuizApi
) : QuizDataSource {
    override suspend fun postCreateQuiz(quizDto: QuizDto): Flow<BaseResponse<Any>> = flow {
        val result = quizApi.postCreateQuiz(quizDto)
        emit(result)
    }.catch { e ->
        Log.e("postCreateQuiz 에러", e.message.toString())
    }

    override suspend fun patchModifyQuiz(editQuizDto: EditQuizDto): Flow<Response<ResponseBody>> = flow {
        val result = quizApi.patchModifyQuiz(editQuizDto)
        emit(result)
    }.catch { e ->
        Log.e("patchModifyQuiz 에러", e.message.toString())
    }

    override suspend fun deleteQuiz(quiz_id: Int): Flow<BaseResponse<Any>> = flow {
        val result = quizApi.deleteQuiz(quiz_id)
        emit(result)
    }.catch { e ->
        Log.e("deleteQuiz 에러", e.message.toString())
    }

    override suspend fun getDistinctQuiz(
        quiz_id: Int
    ): Flow<BaseResponse<DistinctQuizResponse>> = flow {
        val result = quizApi.getDistinctQuiz(quiz_id)
        emit(result)
    }.catch { e ->
        Log.e("getDistinctQuiz 에러", e.message.toString())
    }

    override suspend fun getQuizAll(): Flow<BaseResponse<AllQuizResponse>> = flow {
        val result = quizApi.getAllQuiz()
        emit(result)
    }.catch { e ->
        Log.e("getQuizAll 에러", e.message.toString())
    }


}