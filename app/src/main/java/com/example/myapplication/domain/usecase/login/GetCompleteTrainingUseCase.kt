package com.example.myapplication.domain.usecase.login

import com.example.myapplication.data.repository.remote.request.login.LogInKakaoDto
import com.example.myapplication.data.repository.remote.response.login.LogInKakaoResponse
import com.example.myapplication.domain.repository.login.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCompleteTrainingUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(
    ): Flow<Any> = loginRepository.getCompleteTraining()
}