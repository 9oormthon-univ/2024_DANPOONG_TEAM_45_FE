package com.example.myapplication.domain.repository.quiz

import com.example.myapplication.data.repository.remote.datasource.remote.QuizDataSource
import com.example.myapplication.data.repository.remote.request.quiz.EditQuizDto
import com.example.myapplication.data.repository.remote.request.quiz.QuizDto
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.quiz.AllQuizResponse
import com.example.myapplication.data.repository.remote.response.quiz.DistinctQuizResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

data class QuizRepositoryImpl @Inject constructor(
    private val quizDataSource: QuizDataSource
) : QuizRepository {
    override suspend fun postCreateQuiz(quizDto: QuizDto): Flow<Response<ResponseBody>> =
        quizDataSource.postCreateQuiz(quizDto)


    override suspend fun patchModifyQuiz(editQuizDto: EditQuizDto): Flow<Response<ResponseBody>> =
        quizDataSource.patchModifyQuiz(editQuizDto)


    override suspend fun deleteQuiz(quiz_id: Int): Flow<Response<ResponseBody>> =
        quizDataSource.deleteQuiz(quiz_id)


    override suspend fun getDistinctQuiz(
        quiz_id: Int
    ): Flow<BaseResponse<DistinctQuizResponse>> =
        quizDataSource.getDistinctQuiz(quiz_id)


    override suspend fun getQuizAll(): Flow<BaseResponse<AllQuizResponse>> =
        quizDataSource.getQuizAll()


}
