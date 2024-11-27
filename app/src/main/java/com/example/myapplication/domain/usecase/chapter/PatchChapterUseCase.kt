package com.example.myapplication.domain.usecase.chapter

import com.example.myapplication.data.repository.remote.request.chapter.RegisterChapterDto
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.domain.repository.chapter.ChapterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PatchChapterUseCase @Inject constructor(
    private val chapterRepository: ChapterRepository
) {
    suspend operator fun invoke(
        chapter_id: String, registerChapterDto: RegisterChapterDto
    ): Flow<BaseResponse<Any>> = chapterRepository.modifyChapter(chapter_id, registerChapterDto)
}