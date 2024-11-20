package com.example.myapplication.data.repository.remote.datasourceImpl

import android.util.Log
import com.example.myapplication.data.repository.remote.api.HomeApi
import com.example.myapplication.data.repository.remote.datasource.remote.HomeDataSource
import com.example.myapplication.data.repository.remote.request.home.PatchHomeDTO
import com.example.myapplication.data.repository.remote.response.home.DistinctHomeIdResponse
import com.example.myapplication.data.repository.remote.response.home.HomeAllList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class HomeDataSourceImpl @Inject constructor(
    private val homeApi: HomeApi
) : HomeDataSource {

    override suspend fun postHome(): Flow<Response<ResponseBody>> = flow {
        val result = homeApi.postHome()
        emit(result)
    }.catch { e ->
        Log.e("postHome 에러", e.message.toString())
    }

    override suspend fun getDistinctChapter(home_id: Int): Flow<DistinctHomeIdResponse> = flow {
        val result = homeApi.getDistinctChapter(home_id)
        emit(result)
    }.catch { e ->
        Log.e("getDistinctChapter 에러", e.message.toString())
    }

    override suspend fun deleteHomeId(home_id: String): Flow<Response<ResponseBody>> = flow {
        val result = homeApi.deleteHomeId(home_id)
        emit(result)
    }.catch { e ->
        Log.e("deleteHomeId 에러", e.message.toString())
    }

    override suspend fun patchHomeEdit(
        home_id: String,
        patchHomeDTO: PatchHomeDTO
    ): Flow<Response<ResponseBody>> = flow {
        val result = homeApi.patchHomeEdit(home_id, patchHomeDTO)
        emit(result)
    }.catch { e ->
        Log.e("patchHomeEdit 에러", e.message.toString())
    }

    override suspend fun getAllHome(): Flow<HomeAllList> = flow {
        val result = homeApi.getAllHome()
        emit(result)
    }.catch { e ->
        Log.e("getAllHome 에러", e.message.toString())
    }
}