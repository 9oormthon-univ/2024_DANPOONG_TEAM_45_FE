package com.example.myapplication.data.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.myapplication.data.repository.local.dao.ChapterDao
import com.example.myapplication.data.repository.local.dao.QuizDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ChapterDatabase {
        val database = Room.databaseBuilder(
            context,
            ChapterDatabase::class.java,
            "chapter_database"
        ).fallbackToDestructiveMigration()
            .build()

        return database
    }

    @Provides
    fun provideChapterDao(database: ChapterDatabase): ChapterDao = database.chapterDao()

    @Provides
    fun provideQuizDao(database: ChapterDatabase): QuizDao = database.quizDao()
}