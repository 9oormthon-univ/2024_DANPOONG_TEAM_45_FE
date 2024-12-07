package com.example.myapplication.data.di

import android.util.Log
import com.example.myapplication.BuildConfig
import com.example.myapplication.presentation.widget.extention.AccessTokenInterceptor
import com.example.myapplication.presentation.widget.extention.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideAccessTokenInterceptor(tokenManager: TokenManager): AccessTokenInterceptor {
        return AccessTokenInterceptor(tokenManager)
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    @Named("defaultOkHttpClient")
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        accessTokenInterceptor: AccessTokenInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor{ chain ->
                val request = chain.request()
                Log.d("토큰", "Headers: ${request.headers}")
                chain.proceed(request)
            }
            .addInterceptor(accessTokenInterceptor)
            .build()
    }

    @MainRetrofit
    @Singleton
    @Provides
    fun provideMainRetrofit(
        @Named("defaultOkHttpClient") okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .build()
    }
}
