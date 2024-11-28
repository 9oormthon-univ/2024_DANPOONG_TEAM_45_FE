package com.example.myapplication.data.repository.local.dao

import androidx.room.Transaction
import com.example.myapplication.data.mapper.toChapter
import com.example.myapplication.data.mapper.toQuizzes
import com.example.myapplication.data.repository.local.ChapterDTO
import com.example.myapplication.data.repository.local.QuizDTO
import com.example.myapplication.data.repository.remote.response.chapter.DistinctChapterResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChapterRepository @Inject constructor(
    private val chapterDao: ChapterDao,
    private val quizDao: QuizDao
) {
    @Transaction
    suspend fun saveChaptersWithQuizzes(chapters: List<DistinctChapterResponse>) {
        // 데이터베이스 초기화
        chapterDao.deleteAllChapters()
        quizDao.deleteAllQuizzes()

        // ID 초기화
        chapterDao.resetChapterId()
        quizDao.resetQuizId()

        chapters.forEach { chapterResponse ->
            // Chapter 삽입 후, 삽입된 ID 반환
            val chapterId = chapterDao.insert(chapterResponse.toChapter()).toInt()

            // 반환된 chapterId를 사용해 QuizDTO 생성
            val quizzes = chapterResponse.toQuizzes(chapterId)
            quizDao.insertAll(quizzes) // Quiz 삽입
        }
    }

    // 모든 챕터 가져오기
    suspend fun getAllChapters(): List<ChapterDTO> = chapterDao.getAllChapters()

    // 챕터별 퀴즈 가져오기
    // 특정 챕터의 Quiz를 Flow로 반환
    fun getQuizzesByChapterFlow(chapterId: Int): Flow<List<QuizDTO>> {
        return quizDao.getQuizzesByChapter(chapterId)
    }
}



