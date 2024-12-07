package com.example.myapplication.domain.usecase.quizcleared

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.repository.quizcleared.IsQuizClearedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PatchQuizSuccessStateUseCase @Inject constructor(
    private val quizClearedRepository: IsQuizClearedRepository
) {
    suspend operator fun invoke(isChapterCleared_id: Int,isCleared : Boolean
    ): Flow<BaseResponse<Any>> = quizClearedRepository.patchQuizSuccessState(isChapterCleared_id, isCleared)
}