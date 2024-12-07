package com.example.myapplication.data.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.repository.local.ChapterDTO

@Dao
interface ChapterDao {

    @Query("DELETE FROM ChapterDTO")
    suspend fun deleteAllChapters()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'ChapterDTO'")
    suspend fun resetChapterId()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chapters: List<ChapterDTO>)

    // 단일 ChapterDTO 삽입 후 ID 반환
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chapter: ChapterDTO): Long

    @Query("SELECT * FROM ChapterDTO")
    suspend fun getAllChapters(): List<ChapterDTO>
}

