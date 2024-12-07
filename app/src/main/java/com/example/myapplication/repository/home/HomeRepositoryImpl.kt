package com.example.myapplication.repository.home

import com.example.myapplication.data.repository.remote.datasource.remote.HomeDataSource
import com.example.myapplication.data.repository.remote.request.home.PatchHomeDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.home.DistinctHomeIdResponse
import com.example.myapplication.data.repository.remote.response.home.HomeAllList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeDataSource: HomeDataSource
) : HomeRepository {
    override suspend fun postHome(): Flow<BaseResponse<Any>> =
        homeDataSource.postHome()

    override suspend fun getDistinctHome(): Flow<BaseResponse<DistinctHomeIdResponse>> =
        homeDataSource.getDistinctHome()

    override suspend fun deleteHomeId(home_id: String): Flow<BaseResponse<Any>> =
        homeDataSource.deleteHomeId(home_id)

    override suspend fun patchHomeEdit(
        home_id: String,
        patchHomeDTO: PatchHomeDTO
    ): Flow<BaseResponse<Any>> =
        homeDataSource.patchHomeEdit(home_id, patchHomeDTO)

    override suspend fun getAllHome(): Flow<HomeAllList> =
        homeDataSource.getAllHome()

}