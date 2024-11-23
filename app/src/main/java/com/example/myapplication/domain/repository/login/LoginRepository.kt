package com.example.myapplication.domain.repository.login

import com.example.myapplication.data.repository.remote.request.login.LogInKakaoDto
import com.example.myapplication.data.repository.remote.request.login.UserDTO
import com.example.myapplication.data.repository.remote.request.login.UserListDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.login.LogInKakaoResponse
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun postKakaoLogin(logInKakaoDto: LogInKakaoDto): Flow<LogInKakaoResponse>
    suspend fun patchUsers(userDTO: UserDTO): Flow<BaseResponse<Any>>
    suspend fun getUser(user_id: Int): Flow<BaseResponse<UserDTO>>
    suspend fun getCompleteTraining(): Flow<BaseResponse<Any>>
    suspend fun checkTraining(): Flow<BaseResponse<Boolean>>
    suspend fun getAllUser(): Flow<BaseResponse<UserListDTO>>
}