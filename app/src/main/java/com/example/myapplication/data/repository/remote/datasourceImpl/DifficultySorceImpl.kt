package com.example.codingland.data.repository.remote.datasourceimpl

import android.util.Log
import com.example.myapplication.data.repository.remote.api.DifficultyApi
import com.example.myapplication.data.repository.remote.datasource.remote.DifficultyDatasourceDataSource
import com.example.myapplication.data.repository.remote.response.difficulty.DifficultyLevelList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class DifficultySorceImpl @Inject constructor(
    private val difficultyApi: DifficultyApi
) : DifficultyDatasourceDataSource {

    override suspend fun postDifficultyLevel(level: String): Flow<Response<ResponseBody>> = flow {
        val result = difficultyApi.postDifficultyLevel(level)
        emit(result)
    }.catch { e ->
        Log.e("postDifficultyLevel 에러", e.message.toString())
    }

    override suspend fun deleteDifficultyLevel(difficulty_id: String): Flow<Response<ResponseBody>> =
        flow {
            val result = difficultyApi.deleteDifficultyLevel(difficulty_id)
            emit(result)
        }.catch { e ->
            Log.e("deleteDifficultyLevel 에러", e.message.toString())
        }

    override suspend fun patchDifficultyLevel(
        difficulty_id: String,
        level: Int
    ): Flow<Response<ResponseBody>> = flow {
        val result = difficultyApi.patchDifficultyLevel(difficulty_id, level)
        emit(result)
    }.catch { e ->
        Log.e("patchDifficultyLevel 에러", e.message.toString())
    }

    override suspend fun getDifficultyAllLevel(): Flow<Response<DifficultyLevelList>> = flow {
        val result = difficultyApi.getDifficultyAllLevel()
        emit(result)
    }.catch { e ->
        Log.e("getDifficultyAllLevel 에러", e.message.toString())
    }

}