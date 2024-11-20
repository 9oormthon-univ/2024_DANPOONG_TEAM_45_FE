package com.example.myapplication.domain.repository.quiz

import com.example.myapplication.data.repository.remote.request.quiz.EditQuizDto
import com.example.myapplication.data.repository.remote.request.quiz.QuizDto
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.quiz.AllQuizResponse
import com.example.myapplication.data.repository.remote.response.quiz.DistinctQuizResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response

interface QuizRepository {
    suspend fun postCreateQuiz(quizDto: QuizDto): Flow<Response<ResponseBody>>
    suspend fun patchModifyQuiz(editQuizDto: EditQuizDto): Flow<Response<ResponseBody>>
    suspend fun deleteQuiz(quiz_id: Int): Flow<Response<ResponseBody>>
    suspend fun getDistinctQuiz(quiz_id: Int): Flow<BaseResponse<DistinctQuizResponse>>
    suspend fun getQuizAll(): Flow<BaseResponse<AllQuizResponse>>
}