package com.example.myapplication.domain.repository.difficulty

import com.example.myapplication.data.repository.remote.datasource.remote.DifficultyDatasourceDataSource
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.difficulty.DifficultyLevelList
import com.example.myapplication.domain.repository.difficulty.DifficultyRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

data class DifficultyRepositoryImpl @Inject constructor(
    private val difficultyDataSource: DifficultyDatasourceDataSource
) : DifficultyRepository {
    override suspend fun postDifficultyLevel(level: String): Flow<BaseResponse<Any>> =
        difficultyDataSource.postDifficultyLevel(level)

    override suspend fun deleteDifficultyLevel(difficulty_id: String): Flow<BaseResponse<Any>> =
        difficultyDataSource.deleteDifficultyLevel(difficulty_id)

    override suspend fun patchDifficultyLevel(
        difficulty_id: String,
        level: Int
    ): Flow<Response<ResponseBody>> =
        difficultyDataSource.patchDifficultyLevel(difficulty_id, level)

    override suspend fun getDifficultyAllLevel(): Flow<Response<DifficultyLevelList>> =
        difficultyDataSource.getDifficultyAllLevel()

}
