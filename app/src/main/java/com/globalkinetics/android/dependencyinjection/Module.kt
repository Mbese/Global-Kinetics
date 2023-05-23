package com.globalkinetics.android.dependencyinjection

import android.content.Context
import com.globalkinetics.android.repository.WeatherRepository
import com.globalkinetics.android.service.APIClient.Companion.BASE_URL
import com.globalkinetics.android.service.WeatherService
import com.globalkinetics.android.view.DailyForecastAdapter
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@dagger.Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): WeatherService =
        retrofit.create(WeatherService::class.java)

    @Singleton
    @Provides
    fun provideFavouriteMoviesRepository(apiService: WeatherService, @ApplicationContext appContext: Context) =
        WeatherRepository(apiService, appContext)

    @Singleton
    @Provides
    fun provideDailyForecastAdapter(@ApplicationContext appContext: Context) = DailyForecastAdapter(appContext)
}