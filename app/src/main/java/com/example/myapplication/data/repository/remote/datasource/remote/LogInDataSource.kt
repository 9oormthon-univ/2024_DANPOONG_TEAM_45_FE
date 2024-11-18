package com.example.myapplication.data.repository.remote.datasource.remote

import com.example.myapplication.data.repository.remote.request.login.LogInKakaoDto
import com.example.myapplication.data.repository.remote.response.login.LogInKakaoResponse
import kotlinx.coroutines.flow.Flow

interface LogInDataSource {
    suspend fun postKakaoLogin(logInKakaoDto: LogInKakaoDto): Flow<LogInKakaoResponse>
}