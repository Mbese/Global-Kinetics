package com.globalkinetics.android.repository

import android.Manifest
import android.content.Context
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.globalkinetics.android.BuildConfig
import com.globalkinetics.android.model.CurrentWeather
import com.globalkinetics.android.model.Daily
import com.globalkinetics.android.model.Hourly
import com.globalkinetics.android.service.WeatherService
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherService: WeatherService,
    private val application: Context
) {

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    suspend fun getCurrentWeather(): CurrentWeather? {
        return locationFlow().map {
            weatherService.getCurrentWeather(it.latitude, it.longitude, BuildConfig.API_KEY)
                .body()
        }.filterNotNull().firstOrNull()

    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    suspend fun getFullWeather(): List<Daily>? {
        return locationFlow().map {
            weatherService.getFullWeather(it.latitude, it.longitude, BuildConfig.API_KEY)
                .body()
        }.map {
            it?.daily?.drop(1)?.take(5)
        }.filterNotNull().firstOrNull()
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    suspend fun getHourlyForecast(): List<Hourly>? {
        return locationFlow().map {
            weatherService.getFullWeather(it.latitude, it.longitude, BuildConfig.API_KEY)
                .body()
        }.map {
            it?.hourly?.drop(1)?.take(8)
        }.filterNotNull().firstOrNull()
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private fun locationFlow() = channelFlow {
        val client = LocationServices.getFusedLocationProviderClient(application)
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { trySend(it) }
            }
        }

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(2000)
            .setMaxUpdateDelayMillis(100)
            .build()

        client.requestLocationUpdates(locationRequest, callback, Looper.getMainLooper())

        awaitClose {
            client.removeLocationUpdates(callback)
        }
    }

}