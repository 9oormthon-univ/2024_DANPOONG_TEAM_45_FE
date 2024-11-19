package com.example.codingland.domain.usecase.chapter

import com.example.codingland.data.repository.remote.response.chapter.AllChapterResponse
import com.example.myapplication.domain.repository.chapter.ChapterRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class GetAllChapterUseCase @Inject constructor(
    private val chapterRepository: ChapterRepository
) {
    suspend operator fun invoke(
    ): Flow<Response<AllChapterResponse>> = chapterRepository.getAllChapter()
}