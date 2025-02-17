package com.example.myapplication.repository.quiz

import com.example.myapplication.data.repository.remote.datasource.remote.QuizDataSource
import com.example.myapplication.data.repository.remote.request.quiz.EditQuizDto
import com.example.myapplication.data.repository.remote.request.quiz.QuizDto
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.quiz.AllQuizResponse
import com.example.myapplication.data.repository.remote.response.quiz.DailyCompleteResponse
import com.example.myapplication.data.repository.remote.response.quiz.DistinctQuizResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

data class QuizRepositoryImpl @Inject constructor(
    private val quizDataSource: QuizDataSource
) : QuizRepository {
    override suspend fun postCreateQuiz(quizDto: QuizDto): Flow<BaseResponse<Any>> =
        quizDataSource.postCreateQuiz(quizDto)


    override suspend fun patchModifyQuiz(editQuizDto: EditQuizDto): Flow<Response<ResponseBody>> =
        quizDataSource.patchModifyQuiz(editQuizDto)


    override suspend fun deleteQuiz(quiz_id: Int): Flow<BaseResponse<Any>> =
        quizDataSource.deleteQuiz(quiz_id)


    override suspend fun getDistinctQuiz(
        quiz_id: Int
    ): Flow<BaseResponse<DistinctQuizResponse>> =
        quizDataSource.getDistinctQuiz(quiz_id)

    override suspend fun getQuizAll(): Flow<BaseResponse<AllQuizResponse>> =
        quizDataSource.getQuizAll()

    override suspend fun getCompleteQuest():  Flow<BaseResponse<DailyCompleteResponse>> =
        quizDataSource.getCompleteQuest()

    override suspend fun postCompleteQuest(): Flow<BaseResponse<Any>> =
        quizDataSource.postCompleteQuest()

}
