package com.example.myapplication.data.repository.remote.datasourceImpl

import android.util.Log
import com.example.myapplication.data.repository.remote.request.login.LogInKakaoDto
import com.example.myapplication.data.repository.remote.api.LoginApi
import com.example.myapplication.data.repository.remote.datasource.remote.LogInDataSource
import com.example.myapplication.data.repository.remote.response.login.LogInKakaoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LogInDataSourceImpl @Inject constructor(
    private val loginApi: LoginApi
) : LogInDataSource {
    override suspend fun postKakaoLogin(logInKakaoDto: LogInKakaoDto
    ): Flow<LogInKakaoResponse> = flow {
        val result = loginApi.postKakaoLogin(logInKakaoDto)
        emit(result)
    }.catch { e ->
        Log.e("LogInDataSource 에러", e.message.toString())
    }
}