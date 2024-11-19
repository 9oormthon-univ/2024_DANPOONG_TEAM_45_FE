package com.example.myapplication.domain.repository.home

import com.example.myapplication.data.repository.remote.datasource.remote.HomeDataSource
import com.example.myapplication.data.repository.remote.request.home.PatchHomeDTO
import com.example.myapplication.data.repository.remote.response.home.DistinctHomeIdResponse
import com.example.myapplication.data.repository.remote.response.home.HomeAllList
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeDataSource: HomeDataSource
) : HomeRepository{
    override suspend fun postHome(): Flow<Response<ResponseBody>> = homeDataSource.postHome()

    override suspend fun getDistinctHome(home_id: Int): Flow<DistinctHomeIdResponse> = homeDataSource.getDistinctChapter(home_id)

    override suspend fun deleteHomeId(home_id: String): Flow<Response<ResponseBody>> = homeDataSource.deleteHomeId(home_id)

    override suspend fun patchHomeEdit(
        home_id: String,
        patchHomeDTO: PatchHomeDTO
    ): Flow<Response<ResponseBody>> = homeDataSource.patchHomeEdit(home_id,patchHomeDTO)

    override suspend fun getAllHome(): Flow<HomeAllList> = homeDataSource.getAllHome()

}