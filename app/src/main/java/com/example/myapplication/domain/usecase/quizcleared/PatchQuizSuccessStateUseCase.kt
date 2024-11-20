package com.example.myapplication.domain.usecase.quizcleared

import com.example.myapplication.domain.repository.quizcleared.IsQuizClearedRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class PatchQuizSuccessStateUseCase @Inject constructor(
    private val quizClearedRepository: IsQuizClearedRepository
) {
    suspend operator fun invoke(isChapterCleared_id: Int,isCleared : Boolean
    ): Flow<Response<ResponseBody>> = quizClearedRepository.patchQuizSuccessState(isChapterCleared_id, isCleared)
}