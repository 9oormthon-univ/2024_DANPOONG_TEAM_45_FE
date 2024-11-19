package com.example.myapplication.domain.usecase.chaptercleared

import com.example.myapplication.domain.repository.chaptercleared.ChapterClearedRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class PostChapterClearedUseCase @Inject constructor(
    private val chapterClearedRepository: ChapterClearedRepository
) {
    suspend operator fun invoke(chapterId: Int,user_id: Int
    ): Flow<Response<ResponseBody>> = chapterClearedRepository.postChapterCleared(chapterId, user_id)
}