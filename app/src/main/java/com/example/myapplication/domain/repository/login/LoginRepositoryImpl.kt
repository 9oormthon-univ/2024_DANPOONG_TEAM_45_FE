package com.example.myapplication.domain.repository.login

import com.example.myapplication.data.repository.remote.datasource.remote.LogInDataSource
import com.example.myapplication.data.repository.remote.request.login.LogInKakaoDto
import com.example.myapplication.data.repository.remote.response.login.LogInKakaoResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class LoginRepositoryImpl @Inject constructor(
    private val logInDataSource: LogInDataSource
) : LoginRepository {
    override suspend fun postKakaoLogin(logInKakaoDto: LogInKakaoDto): Flow<LogInKakaoResponse> =
        logInDataSource.postKakaoLogin(logInKakaoDto)
}
