package com.example.myapplication.domain.usecase.difficulty

import com.example.myapplication.data.repository.remote.response.difficulty.DifficultyLevelList
import com.example.myapplication.repository.difficulty.DifficultyRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class GetDifficultyAllLevel @Inject constructor(
    private val difficultyRepository: DifficultyRepository
) {
    suspend operator fun invoke(
    ): Flow<Response<DifficultyLevelList>> = difficultyRepository.getDifficultyAllLevel()
}