package com.example.myapplication.domain.usecase.home

import com.example.myapplication.data.repository.remote.request.home.PatchHomeDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.domain.repository.home.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PatchHomeEditUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(
        home_id: String,
        patchHomeDTO: PatchHomeDTO
    ): Flow<BaseResponse<Any>> = homeRepository.patchHomeEdit(home_id, patchHomeDTO)
}