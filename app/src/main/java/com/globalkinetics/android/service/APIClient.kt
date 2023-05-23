package com.globalkinetics.android.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {
    companion object {
        const val BASE_URL = "https://api.openweathermap.org/"
        private fun getRetrofitInstance(): Retrofit.Builder {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
        }

        fun getWeatherService(): WeatherService {
            return getRetrofitInstance().build().create(WeatherService::class.java)
        }
    }
}