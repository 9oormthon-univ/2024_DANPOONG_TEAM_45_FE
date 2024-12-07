package com.example.myapplication.domain.usecase.chapter

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.repository.chapter.ChapterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RewardUseCase @Inject constructor(
    private val chapterRepository: ChapterRepository
) {
    suspend operator fun invoke(
        chapter_id: Int
    ): Flow<BaseResponse<Any>> = chapterRepository.rewardSuccess(chapter_id)
}