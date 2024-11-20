package com.example.myapplication.domain.repository.chapter
import com.example.myapplication.data.repository.remote.datasource.remote.ChapterDataSource
import com.example.myapplication.data.repository.remote.request.chapter.RegisterChapterDto
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.chapter.AllChapterResponse
import com.example.myapplication.data.repository.remote.response.chapter.DistinctChapterResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

data class ChapterRepositoryImpl @Inject constructor(
    private val chapterDataSource: ChapterDataSource
) : ChapterRepository {
    override suspend fun postCreateChapter(registerChapterDto: RegisterChapterDto): Flow<Response<ResponseBody>> =
        chapterDataSource.postCreateChapter(registerChapterDto)

    override suspend fun getDistinctChapter(
        chapter_id: Int,
        user_id: Int
    ): Flow<BaseResponse<DistinctChapterResponse>> =
        chapterDataSource.getDistinctChapter(
            chapter_id,
            user_id
        )

    override suspend fun deleteChapter(lchapter_id: String): Flow<Response<ResponseBody>> =
        chapterDataSource.deleteChapter(lchapter_id)

    override suspend fun modifyChapter(registerChapterDto: RegisterChapterDto): Flow<Response<ResponseBody>> =
        chapterDataSource.modifyChapter(registerChapterDto)

    override suspend fun getAllChapter(): Flow<Response<AllChapterResponse>> =
        chapterDataSource.getAllChapter()
}
