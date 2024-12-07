package com.example.myapplication.domain.usecase.chapter

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.chapter.AllChapterResponse
import com.example.myapplication.repository.chapter.ChapterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllChapterUseCase @Inject constructor(
    private val chapterRepository: ChapterRepository
) {
    suspend operator fun invoke(
    ): Flow<BaseResponse<AllChapterResponse>> = chapterRepository.getAllChapter()
}