package com.example.myapplication.domain.usecase.home

import com.example.myapplication.data.repository.remote.response.home.DistinctHomeIdResponse
import com.example.myapplication.data.repository.remote.response.home.HomeAllList
import com.example.myapplication.domain.repository.home.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDistinctHomeUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(home_id: Int): Flow<DistinctHomeIdResponse> = homeRepository.getDistinctHome(home_id)
}