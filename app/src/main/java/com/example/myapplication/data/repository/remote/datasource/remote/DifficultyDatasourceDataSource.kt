package com.example.myapplication.data.repository.remote.datasource.remote

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.difficulty.DifficultyLevelList
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response

interface DifficultyDatasourceDataSource {
    suspend fun postDifficultyLevel(level: String): Flow<BaseResponse<Any>>
    suspend fun deleteDifficultyLevel(difficulty_id: String): Flow<BaseResponse<Any>>
    suspend fun patchDifficultyLevel(
        difficulty_id: String,
        level: Int
    ): Flow<Response<ResponseBody>>

    suspend fun getDifficultyAllLevel(): Flow<Response<DifficultyLevelList>>
}