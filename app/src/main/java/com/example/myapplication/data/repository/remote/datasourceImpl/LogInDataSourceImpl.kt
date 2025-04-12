package com.example.myapplication.data.repository.remote.datasourceImpl

import android.util.Log
import com.example.myapplication.data.repository.remote.api.LoginApi
import com.example.myapplication.data.repository.remote.datasource.remote.LogInDataSource
import com.example.myapplication.data.repository.remote.request.login.LogInKakaoDto
import com.example.myapplication.data.repository.remote.request.login.UserDTO
import com.example.myapplication.data.repository.remote.request.login.UserListDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.login.LogInKakaoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LogInDataSourceImpl @Inject constructor(
    private val loginApi: LoginApi
) : LogInDataSource {
    override suspend fun postKakaoLogin(
        logInKakaoDto: LogInKakaoDto
    ): Flow<LogInKakaoResponse> = flow {
        val result = loginApi.postKakaoLogin(logInKakaoDto)
        emit(result)
    }.catch { e ->
        Log.e("LogInDataSource 에러", e.message.toString())
        emit(LogInKakaoResponse())
    }

    override suspend fun patchUsers(userDTO: UserDTO): Flow<BaseResponse<Any>> = flow {
        val result = loginApi.patchUsers(userDTO)
        emit(result)
    }.catch { e ->
        Log.e("LogInDataSource 에러", e.message.toString())
    }

    override suspend fun getUser(user_id: Int): Flow<BaseResponse<UserDTO>> = flow {
        val result = loginApi.getUser(user_id)
        emit(result)
    }.catch { e ->
        Log.e("LogInDataSource 에러", e.message.toString())
    }

    override suspend fun getCompleteTraining(): Flow<BaseResponse<Any>> = flow {
        val result = loginApi.getCompleteTraining()
        emit(result)
    }.catch { e ->
        Log.e("LogInDataSource 에러", e.message.toString())
    }

    override suspend fun checkTraining(): Flow<BaseResponse<Boolean>> = flow {
        val result = loginApi.checkTraining()
        emit(result)
    }.catch { e ->
        Log.e("LogInDataSource 에러", e.message.toString())
    }

    override suspend fun getAllUser(): Flow<BaseResponse<UserListDTO>> = flow {
        val result = loginApi.getAllUser()
        emit(result)
    }.catch { e ->
        Log.e("LogInDataSource 에러", e.message.toString())
    }

    override suspend fun deleteUser(user_id: Int): Flow<BaseResponse<Any>> = flow {
        val result = loginApi.deleteUser(user_id)
        emit(result)
    }.catch { e ->
        Log.e("LogInDataSource 에러", e.message.toString())
    }
}