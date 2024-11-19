package com.example.myapplication.domain.usecase.chaptercleared
import com.example.myapplication.data.repository.remote.response.chaptercleared.ClearChapterStateListResponse
import com.example.myapplication.domain.repository.chaptercleared.ChapterClearedRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class GetChapterAllUseCase @Inject constructor(
    private val chapterClearedRepository: ChapterClearedRepository
) {
    suspend operator fun invoke(
    ): Flow<Response<ClearChapterStateListResponse>> = chapterClearedRepository.getChapterAll()
}