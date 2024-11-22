package com.example.myapplication.data.di

import com.example.myapplication.data.repository.remote.api.ChapterApi
import com.example.myapplication.data.repository.remote.api.ChapterClearedApi
import com.example.myapplication.data.repository.remote.api.CharacterApi
import com.example.myapplication.data.repository.remote.api.DifficultyApi
import com.example.myapplication.data.repository.remote.api.HomeApi
import com.example.myapplication.data.repository.remote.api.LoginApi
import com.example.myapplication.data.repository.remote.api.QuizApi
import com.example.myapplication.data.repository.remote.api.QuizClearedApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideLogInServer(
        @MainRetrofit retrofit: Retrofit
    ): LoginApi = retrofit.create(LoginApi::class.java)

    @Provides
    @Singleton
    fun provideQuizServer(
        @MainRetrofit retrofit: Retrofit
    ): QuizApi = retrofit.create(QuizApi::class.java)

    @Provides
    @Singleton
    fun provideHomeServer(
        @MainRetrofit retrofit: Retrofit
    ): HomeApi = retrofit.create(HomeApi::class.java)

    @Provides
    @Singleton
    fun provideCharacterServer(
        @MainRetrofit retrofit: Retrofit
    ): CharacterApi = retrofit.create(CharacterApi::class.java)

    @Provides
    @Singleton
    fun provideChapterServer(
        @MainRetrofit retrofit: Retrofit
    ): ChapterApi = retrofit.create(ChapterApi::class.java)

    @Provides
    @Singleton
    fun provideDifficultyServer(
        @MainRetrofit retrofit: Retrofit
    ): DifficultyApi = retrofit.create(DifficultyApi::class.java)

    @Provides
    @Singleton
    fun provideChapterClearServer(
        @MainRetrofit retrofit: Retrofit
    ): ChapterClearedApi = retrofit.create(ChapterClearedApi::class.java)

    @Provides
    @Singleton
    fun provideQuizClearServer(
        @MainRetrofit retrofit: Retrofit
    ): QuizClearedApi = retrofit.create(QuizClearedApi::class.java)

}