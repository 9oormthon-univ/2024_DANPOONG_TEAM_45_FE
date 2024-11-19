package com.example.myapplication.domain.usecase.difficulty
import com.example.myapplication.domain.repository.difficulty.DifficultyRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class PostDifficultyLevel @Inject constructor(
    private val difficultyRepository: DifficultyRepository
) {
    suspend operator fun invoke(level : String
    ): Flow<Response<ResponseBody>> = difficultyRepository.postDifficultyLevel(level)
}