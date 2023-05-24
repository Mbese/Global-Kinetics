package com.globalkinetics.android.viewmodel

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.globalkinetics.android.model.CurrentWeather
import com.globalkinetics.android.model.Daily
import com.globalkinetics.android.model.Hourly
import com.globalkinetics.android.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _showTopLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showTopLoading: MutableLiveData<Boolean>
        get() = _showTopLoading

    private val _showForecastLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showForecastLoading: MutableLiveData<Boolean>
        get() = _showForecastLoading

    private val _showHourlyForecastLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showHourlyForecastLoading: MutableLiveData<Boolean>
        get() = _showHourlyForecastLoading

    private val _showErrorMessage: MutableLiveData<Boolean> = MutableLiveData()
    val showErrorMessage: MutableLiveData<Boolean>
        get() = _showErrorMessage

    private val _currentWeather: MutableLiveData<CurrentWeather> = MutableLiveData()
    val currentWeather: MutableLiveData<CurrentWeather>
        get() = _currentWeather

    private val _fullWeather: MutableLiveData<List<Daily>> = MutableLiveData()
    val fullWeather: MutableLiveData<List<Daily>>
        get() = _fullWeather

    private val _hourlyForecast: MutableLiveData<List<Hourly>> = MutableLiveData()
    val hourlyForecast: MutableLiveData<List<Hourly>>
        get() = _hourlyForecast

    init {
        _showTopLoading.value = true
        _showForecastLoading.value = true
        _showHourlyForecastLoading.value = true
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    suspend fun getCurrentWeather() {
        val currentWeather = repository.getCurrentWeather()
        withContext(Dispatchers.Main) {
            currentWeather?.let {
                _currentWeather.value = it
            }

            if (currentWeather == null) {
                _showErrorMessage.value = true
            }
        }
        _showTopLoading.value = false
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    suspend fun getFullWeather() {
        val fullWeather = repository.getFullWeather()
        withContext(Dispatchers.Main) {
            fullWeather?.let {
                _fullWeather.value = it
            }

            if (fullWeather == null) {
                _showErrorMessage.value = true
            }
        }
        showForecastLoading.value = false
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    suspend fun getHourlyForecast() {
        val fullWeather = repository.getHourlyForecast()
        withContext(Dispatchers.Main) {
            fullWeather?.let {
                _hourlyForecast.value = it
            }

            if (fullWeather == null) {
                _showErrorMessage.value = true
            }
        }
        _showHourlyForecastLoading.value = false
    }
}