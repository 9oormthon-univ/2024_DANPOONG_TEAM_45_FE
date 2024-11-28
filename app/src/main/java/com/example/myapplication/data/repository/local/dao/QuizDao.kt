package com.example.myapplication.data.repository.local.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.repository.local.QuizDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Query("DELETE FROM QuizDTO")
    suspend fun deleteAllQuizzes()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'QuizDTO'")
    suspend fun resetQuizId()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(quizzes: List<QuizDTO>)

    @Query("SELECT * FROM QuizDTO WHERE chapterId = :chapterId")
    fun getQuizzesByChapter(chapterId: Int): Flow<List<QuizDTO>>

}

