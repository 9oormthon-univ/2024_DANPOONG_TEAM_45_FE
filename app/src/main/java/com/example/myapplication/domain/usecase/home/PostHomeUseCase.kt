package com.example.myapplication.domain.usecase.home

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.domain.repository.home.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostHomeUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): Flow<BaseResponse<Any>> = homeRepository.postHome()
}