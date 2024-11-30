package com.example.myapplication.data.di

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.repository.local.ChapterDTO
import com.example.myapplication.data.repository.local.QuizDTO
import com.example.myapplication.data.repository.local.dao.ChapterDao
import com.example.myapplication.data.repository.local.dao.QuizDao

@Database(entities = [ChapterDTO::class, QuizDTO::class], version = 1)
abstract class ChapterDatabase : RoomDatabase() {
    abstract fun chapterDao(): ChapterDao
    abstract fun quizDao(): QuizDao
}

