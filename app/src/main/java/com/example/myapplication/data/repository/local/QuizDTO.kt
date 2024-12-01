package com.example.myapplication.data.repository.local
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ChapterDTO::class,
            parentColumns = ["cid"],
            childColumns = ["chapterId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["chapterId"])] // 외래 키 인덱스 추가
)
data class QuizDTO(
    @PrimaryKey(autoGenerate = true) var qid: Int = 0,
    @ColumnInfo(name = "chapterId") val chapterId: Int,
    @ColumnInfo(name = "quizId") val quizId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "level") val level: Int,
    @ColumnInfo(name = "isCleared") val isCleared: Boolean
)
