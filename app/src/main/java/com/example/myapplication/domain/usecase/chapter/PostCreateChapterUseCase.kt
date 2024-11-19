package com.example.myapplication.domain.usecase.chapter

import com.example.myapplication.data.repository.remote.request.chapter.RegisterChapterDto
import com.example.myapplication.domain.repository.chapter.ChapterRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class PostCreateChapterUseCase @Inject constructor(
    private val chapterRepository: ChapterRepository
) {
    suspend operator fun invoke(registerChapterDto: RegisterChapterDto
    ): Flow<Response<ResponseBody>> = chapterRepository.postCreateChapter(registerChapterDto)
}