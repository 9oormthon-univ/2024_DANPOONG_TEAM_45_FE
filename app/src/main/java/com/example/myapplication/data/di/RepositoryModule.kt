package com.example.myapplication.data.di

import com.example.myapplication.data.repository.remote.api.ChapterApi
import com.example.myapplication.data.repository.remote.api.CharacterApi
import com.example.myapplication.data.repository.remote.api.DifficultyApi
import com.example.myapplication.data.repository.remote.api.HomeApi
import com.example.myapplication.data.repository.remote.api.LoginApi
import com.example.myapplication.data.repository.remote.api.QuizApi
import com.example.myapplication.data.repository.remote.datasource.remote.ChapterDataSource
import com.example.myapplication.data.repository.remote.datasource.remote.CharacterDataSource
import com.example.myapplication.data.repository.remote.datasource.remote.DifficultyDatasourceDataSource
import com.example.myapplication.data.repository.remote.datasource.remote.HomeDataSource
import com.example.myapplication.data.repository.remote.datasource.remote.LogInDataSource
import com.example.myapplication.data.repository.remote.datasource.remote.QuizDataSource
import com.example.myapplication.data.repository.remote.datasourceImpl.ChapterDataSorceImpl
import com.example.myapplication.data.repository.remote.datasourceImpl.CharacterDataSourceImpl
import com.example.myapplication.data.repository.remote.datasourceImpl.DifficultySorceImpl
import com.example.myapplication.data.repository.remote.datasourceImpl.HomeDataSourceImpl
import com.example.myapplication.data.repository.remote.datasourceImpl.LogInDataSourceImpl
import com.example.myapplication.data.repository.remote.datasourceImpl.QuizDataSorceImpl
import com.example.myapplication.domain.repository.chapter.ChapterRepository
import com.example.myapplication.domain.repository.chapter.ChapterRepositoryImpl
import com.example.myapplication.domain.repository.character.CharacterRepository
import com.example.myapplication.domain.repository.character.CharacterRepositoryImpl
import com.example.myapplication.domain.repository.difficulty.DifficultyRepository
import com.example.myapplication.domain.repository.difficulty.DifficultyRepositoryImpl
import com.example.myapplication.domain.repository.home.HomeRepository
import com.example.myapplication.domain.repository.home.HomeRepositoryImpl
import com.example.myapplication.domain.repository.login.LoginRepository
import com.example.myapplication.domain.repository.login.LoginRepositoryImpl
import com.example.myapplication.domain.repository.quiz.QuizRepository
import com.example.myapplication.domain.repository.quiz.QuizRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLogInDataSource(
        loginApi: LoginApi
    ): LogInDataSource {
        return LogInDataSourceImpl(loginApi)
    }

    @Singleton
    @Provides
    fun provideAccountRepository(logInDataSource: LogInDataSource): LoginRepository =
        LoginRepositoryImpl(logInDataSource)

    @Provides
    @Singleton
    fun provideQuizDataSource(
        quizApi: QuizApi
    ): QuizDataSource {
        return QuizDataSorceImpl(quizApi)
    }

    @Singleton
    @Provides
    fun provideQuizRepository(quizDataSource: QuizDataSource): QuizRepository =
        QuizRepositoryImpl(quizDataSource)

    @Provides
    @Singleton
    fun provideHomeDataSource(
        homeApi: HomeApi
    ): HomeDataSource {
        return HomeDataSourceImpl(homeApi)
    }

    @Singleton
    @Provides
    fun provideHomeRepository(homeDataSource: HomeDataSource): HomeRepository =
        HomeRepositoryImpl(homeDataSource)

    @Provides
    @Singleton
    fun provideCharacterDataSource(
        characterApi: CharacterApi
    ): CharacterDataSource {
        return CharacterDataSourceImpl(characterApi)
    }

    @Singleton
    @Provides
    fun provideCharacterRepository(characterDataSource: CharacterDataSource): CharacterRepository =
        CharacterRepositoryImpl(characterDataSource)

    @Provides
    @Singleton
    fun provideChapterDataSource(
        chapterApi: ChapterApi
    ): ChapterDataSource {
        return ChapterDataSorceImpl(chapterApi)
    }

    @Singleton
    @Provides
    fun provideChapterRepository(chapterDataSource: ChapterDataSource): ChapterRepository =
        ChapterRepositoryImpl(chapterDataSource)

    @Provides
    @Singleton
    fun provideDifficultyDataSource(
        difficultyApi: DifficultyApi
    ): DifficultyDatasourceDataSource {
        return DifficultySorceImpl(difficultyApi)
    }

    @Singleton
    @Provides
    fun provideDifficultyRepository(difficultyDatasourceDataSource: DifficultyDatasourceDataSource): DifficultyRepository =
        DifficultyRepositoryImpl(difficultyDatasourceDataSource)
}
