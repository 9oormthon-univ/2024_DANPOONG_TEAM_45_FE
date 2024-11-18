package com.example.myapplication.domain.repository.login

import com.example.myapplication.data.repository.remote.request.login.LogInKakaoDto
import com.example.myapplication.data.repository.remote.response.login.LogInKakaoResponse
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun postKakaoLogin(logInKakaoDto: LogInKakaoDto) : Flow<LogInKakaoResponse>
}