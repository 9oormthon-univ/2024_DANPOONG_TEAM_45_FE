package com.example.myapplication.data.di

import com.example.myapplication.data.repository.remote.api.LoginApi
import com.example.myapplication.data.repository.remote.api.QuizApi
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

}