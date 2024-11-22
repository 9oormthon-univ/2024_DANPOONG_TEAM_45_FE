package com.example.myapplication.domain.usecase.chapter

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.domain.repository.chapter.ChapterRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class DeleteChapterUseCase @Inject constructor(
    private val chapterRepository: ChapterRepository
) {
    suspend operator fun invoke(
        chapter_id: String
    ): Flow<BaseResponse<Any>> = chapterRepository.deleteChapter(chapter_id)
}