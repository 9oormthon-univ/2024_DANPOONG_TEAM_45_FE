package com.example.myapplication.domain.usecase.difficulty

import com.example.myapplication.repository.difficulty.DifficultyRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class PatchDifficultyLevel @Inject constructor(
    private val difficultyRepository: DifficultyRepository
) {
    suspend operator fun invoke(difficulty_id : String,level : Int
    ): Flow<Response<ResponseBody>> = difficultyRepository.patchDifficultyLevel(difficulty_id,level)
}