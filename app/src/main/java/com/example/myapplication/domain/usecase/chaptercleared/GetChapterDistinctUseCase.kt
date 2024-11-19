package com.example.myapplication.domain.usecase.chaptercleared
import com.example.myapplication.data.repository.remote.response.chaptercleared.ClearChapterStateResponse
import com.example.myapplication.domain.repository.chaptercleared.ChapterClearedRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class GetChapterDistinctUseCase @Inject constructor(
    private val chapterClearedRepository: ChapterClearedRepository
) {
    suspend operator fun invoke(isChapterCleared_id: Int
    ): Flow<Response<ClearChapterStateResponse>> = chapterClearedRepository.getChapterDistinct(isChapterCleared_id)
}