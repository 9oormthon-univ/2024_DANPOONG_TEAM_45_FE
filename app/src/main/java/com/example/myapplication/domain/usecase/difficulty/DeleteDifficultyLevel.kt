package com.example.myapplication.domain.usecase.difficulty
import com.example.myapplication.domain.repository.difficulty.DifficultyRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class DeleteDifficultyLevel @Inject constructor(
    private val difficultyRepository: DifficultyRepository
) {
    suspend operator fun invoke(difficulty_id  : String
    ): Flow<Response<ResponseBody>> = difficultyRepository.deleteDifficultyLevel(difficulty_id)
}