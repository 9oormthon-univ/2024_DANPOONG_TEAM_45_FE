package com.example.myapplication.domain.usecase.chaptercleared

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.repository.chaptercleared.ChapterClearedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostChapterClearedUseCase @Inject constructor(
    private val chapterClearedRepository: ChapterClearedRepository
) {
    suspend operator fun invoke(chapterId: Int
    ): Flow<BaseResponse<Any>> = chapterClearedRepository.postChapterCleared(chapterId)
}