package com.example.myapplication.data.di

import com.example.myapplication.data.repository.remote.api.LoginApi
import com.example.myapplication.data.repository.remote.datasource.remote.LogInDataSource
import com.example.myapplication.data.repository.remote.datasourceImpl.LogInDataSourceImpl
import com.example.myapplication.domain.repository.login.LoginRepository
import com.example.myapplication.domain.repository.login.LoginRepositoryImpl
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
}
