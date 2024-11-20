package com.example.myapplication.domain.usecase.home

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.domain.repository.home.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteHomeIdUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(home_id: String): Flow<BaseResponse<Any>> = homeRepository.deleteHomeId(home_id)
}