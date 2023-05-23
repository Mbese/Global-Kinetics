package com.globalkinetics.android.service

import com.globalkinetics.android.model.CurrentWeather
import com.globalkinetics.android.model.FullWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("data/2.5/weather?units=metric")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("appid") appid: String
    ): Response<CurrentWeather>

    @GET("data/2.5/onecall?units=metric&exclude=minutely,alerts")
    suspend fun getFullWeather(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("appid") appid: String
    ): Response<FullWeather>
}

