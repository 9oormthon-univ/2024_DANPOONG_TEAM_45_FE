package com.example.myapplication.domain.usecase.login

import com.example.myapplication.data.repository.remote.request.login.UserDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.repository.login.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PatchUsersUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(
        userDTO: UserDTO
    ): Flow<BaseResponse<Any>> = loginRepository.patchUsers(userDTO)
}