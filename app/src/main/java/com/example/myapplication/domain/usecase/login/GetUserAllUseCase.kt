package com.example.myapplication.domain.usecase.login

import com.example.myapplication.data.repository.remote.request.login.UserListDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.repository.login.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserAllUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(
    ): Flow<BaseResponse<UserListDTO>> = loginRepository.getAllUser()
}
