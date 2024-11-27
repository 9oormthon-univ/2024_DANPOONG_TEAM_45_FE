package com.example.myapplication.domain.usecase.login

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.domain.repository.login.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(
        user_id: Int
    ): Flow<BaseResponse<Any>> = loginRepository.deleteUser(user_id)
}