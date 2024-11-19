package com.example.myapplication.domain.usecase.chaptercleared

import com.example.myapplication.domain.repository.chaptercleared.ChapterClearedRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class PutChapterClearedUseCase @Inject constructor(
    private val chapterClearedRepository: ChapterClearedRepository
) {
    suspend operator fun invoke(isChapterCleared_id: Int,isCleared : Boolean
    ): Flow<Response<ResponseBody>> = chapterClearedRepository.putChapterCleared(isChapterCleared_id, isCleared)
}