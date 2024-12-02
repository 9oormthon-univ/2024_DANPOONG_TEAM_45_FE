package com.example.myapplication.domain.repository.login

import com.example.myapplication.data.repository.remote.datasource.remote.LogInDataSource
import com.example.myapplication.data.repository.remote.request.login.LogInKakaoDto
import com.example.myapplication.data.repository.remote.request.login.UserDTO
import com.example.myapplication.data.repository.remote.request.login.UserListDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.login.LogInKakaoResponse
import com.example.myapplication.repository.login.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class LoginRepositoryImpl @Inject constructor(
    private val logInDataSource: LogInDataSource
) : LoginRepository {
    override suspend fun postKakaoLogin(logInKakaoDto: LogInKakaoDto): Flow<LogInKakaoResponse> =
        logInDataSource.postKakaoLogin(logInKakaoDto)

    override suspend fun patchUsers(userDTO: UserDTO): Flow<BaseResponse<Any>> =
        logInDataSource.patchUsers(userDTO)

    override suspend fun getUser(user_id: Int): Flow<BaseResponse<UserDTO>> =
        logInDataSource.getUser(user_id)

    override suspend fun getCompleteTraining(): Flow<BaseResponse<Any>> =
        logInDataSource.getCompleteTraining()

    override suspend fun checkTraining(): Flow<BaseResponse<Boolean>> =
        logInDataSource.checkTraining()

    override suspend fun getAllUser(): Flow<BaseResponse<UserListDTO>> = logInDataSource.getAllUser()
    override suspend fun deleteUser(user_id: Int): Flow<BaseResponse<Any>> = logInDataSource.deleteUser(user_id)
}
