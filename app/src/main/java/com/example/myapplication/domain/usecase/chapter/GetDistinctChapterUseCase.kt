package com.example.myapplication.domain.usecase.chapter

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.chapter.DistinctChapterResponse
import com.example.myapplication.domain.repository.chapter.ChapterRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject


class GetDistinctChapterUseCase @Inject constructor(
    private val chapterRepository: ChapterRepository
) {
    suspend operator fun invoke(
        chapter_id: Int, user_id: Int
    ): Flow<BaseResponse<DistinctChapterResponse>> =
        chapterRepository.getDistinctChapter(chapter_id, user_id)
}