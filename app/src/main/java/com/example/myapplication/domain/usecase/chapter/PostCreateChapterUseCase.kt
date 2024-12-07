package com.example.myapplication.domain.usecase.chapter

import com.example.myapplication.data.repository.remote.request.chapter.RegisterChapterDto
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.repository.chapter.ChapterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostCreateChapterUseCase @Inject constructor(
    private val chapterRepository: ChapterRepository
) {
    suspend operator fun invoke(registerChapterDto: RegisterChapterDto
    ): Flow<BaseResponse<Any>> = chapterRepository.postCreateChapter(registerChapterDto)
}