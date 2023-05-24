package com.globalkinetics.android.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.globalkinetics.android.model.*
import com.globalkinetics.android.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    @Mock
    private lateinit var mockRepository: WeatherRepository
    private lateinit var viewModel: WeatherViewModel

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = WeatherViewModel(mockRepository)
    }

    @Test
    fun `test on init showTopLoading is true`() {
        viewModel.showTopLoading.value?.let { assertTrue(it) }
    }

    @Test
    fun `test on init showForecastLoading is true`() {
        viewModel.showForecastLoading.value?.let { assertTrue(it) }
    }

    @Test
    fun `test on init showErrorMessage is false`() {
        viewModel.showErrorMessage.value?.let { assertFalse(it) }
    }

    @Test
    fun `test on init showHourlyForecastLoading is true`() {
        viewModel.showHourlyForecastLoading.value?.let { assertTrue(it) }
    }

    @Test
    fun `test on getCurrentWeather call finished showTopLoading is false`() {
        runTest {
            viewModel.getCurrentWeather()
            viewModel.showTopLoading.value?.let { assertFalse(it) }
        }
    }

    @Test
    fun `test on getFullWeather call finished showForecastLoading is false`() {
        runTest {
            viewModel.getFullWeather()
            viewModel.showForecastLoading.value?.let { assertFalse(it) }
        }
    }

    @Test
    fun `test on getHourlyForecast call finished showHourlyForecastLoading is false`() {
        runTest {
            viewModel.getHourlyForecast()
            viewModel.showHourlyForecastLoading.value?.let { assertFalse(it) }
        }
    }

    @Test
    fun `test on getCurrentWeather valid response currentWeather is set`() {
        val currentWeather = CurrentWeather(
            coord = Coord(23.12, 38.01),
            weather = listOf(),
            base = "base",
            main = Main("temp", 1.1, 1.2, 2.3, 0, 0),
            visibility = 0,
            wind = Wind(1.0, 0, 0.0),
            clouds = Clouds(0),
            dt = 1L,
            sys = Sys(0, 0, "country", 0, 0),
            timezone = 0,
            id = 0,
            name = "name",
            cod = 0
        )
        runTest {
            Mockito.`when`(mockRepository.getCurrentWeather()).thenReturn(currentWeather)

            viewModel.getCurrentWeather()
            assertEquals(viewModel.currentWeather.value, currentWeather)
        }
    }

    @Test
    fun `test on getFullWeather valid response fullWeather is set`() {
        val dailyForecast = listOf(Daily(
            dt = 1684807200,
            sunrise = 1684784149,
            sunset = 1684835151,
            moonrise = 1684792800,
            moonset = 1684848120,
            moonPhase = 0.11,
            temp = Temp(
                day = 12.95,
                min = 11.51,
                max = 15.72,
                night = 11.51,
                eve = 12.76,
                morn = 14.22
            ),
            feelsLike = FeelsLike(
                day = 12.81,
                night = 11.2,
                eve = 12.62,
                morn = 14.13
            ),
            pressure = 1000,
            humidity = 96,
            dewPoint = 12.33,
            windSpeed = 5.04,
            windDeg = 58,
            windGust = 7.6,
            weather = arrayListOf(),
            clouds = 100,
            pop = 1.1,
            rain = 12.66,
            uvi = 0.61
        ))
        runTest {
            Mockito.`when`(mockRepository.getFullWeather()).thenReturn(dailyForecast)

            viewModel.getFullWeather()
            assertEquals(viewModel.fullWeather.value, dailyForecast)
        }
    }

    @Test
    fun `test on getCurrentWeather null response showErrorMessage is false`() {
        runTest {
            Mockito.`when`(mockRepository.getCurrentWeather()).thenReturn(null)

            viewModel.getCurrentWeather()
            viewModel.showErrorMessage.value?.let { assertTrue(it) }
        }
    }

    @Test
    fun `test on getFullWeather null response showErrorMessage is false`() {
        runTest {
            Mockito.`when`(mockRepository.getCurrentWeather()).thenReturn(null)

            viewModel.getCurrentWeather()
            viewModel.showErrorMessage.value?.let { assertTrue(it) }
        }
    }

    @Test
    fun `test on getHourlyForecast null response showErrorMessage is false`() {
        runTest {
            Mockito.`when`(mockRepository.getHourlyForecast()).thenReturn(null)

            viewModel.getHourlyForecast()
            viewModel.showErrorMessage.value?.let { assertTrue(it) }
        }
    }
}