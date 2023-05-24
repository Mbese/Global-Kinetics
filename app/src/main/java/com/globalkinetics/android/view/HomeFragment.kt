package com.globalkinetics.android.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.globalkinetics.android.BuildConfig
import com.globalkinetics.android.R
import com.globalkinetics.android.model.CurrentWeather
import com.globalkinetics.android.model.Daily
import com.globalkinetics.android.model.Hourly
import com.globalkinetics.android.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel: WeatherViewModel by viewModels()

    @Inject
    lateinit var dailyForecastAdapter: DailyForecastAdapter

    @Inject
    lateinit var hourlForecastAdapter: HourlForecastAdapter

    @Inject
    lateinit var popAdapter: PopAdapter

    private var recyclerView: RecyclerView? = null
    private var hourlyForecastRecyclerView: RecyclerView? = null
    private var popRecyclerView: RecyclerView? = null
    private lateinit var topViewProgressBar: ProgressBar
    private lateinit var dailyForecastProgressBar: ProgressBar
    private lateinit var hourlyForecastProgressBar: ProgressBar
    private lateinit var currentWeatherView: ViewGroup
    private lateinit var locationTextView: TextView
    private lateinit var tempTextView: TextView
    private lateinit var weatherConditionDescTextView: TextView
    private lateinit var weatherConditionIconImageView: ImageView
    private lateinit var weatherMapImageView: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handlePermissions(view)

        recyclerView = view.findViewById(R.id.day_forecast_recycler_view)
        recyclerView?.setHasFixedSize(true)

        hourlyForecastRecyclerView = view.findViewById(R.id.hourly_forecast_recycler_view)
        hourlyForecastRecyclerView?.setHasFixedSize(true)

        popRecyclerView = view.findViewById(R.id.pop_forecast_recycler_view)
        popRecyclerView?.setHasFixedSize(true)

        topViewProgressBar = view.findViewById(R.id.top_view_progress_loader)
        dailyForecastProgressBar = view.findViewById(R.id.daily_forecast_progress_bar)
        hourlyForecastProgressBar = view.findViewById(R.id.hourly_forecast_progress_bar)
        currentWeatherView = view.findViewById(R.id.current_weather_view)
        locationTextView = view.findViewById(R.id.location_text_view)
        tempTextView = view.findViewById(R.id.temperature_text_view)
        weatherConditionDescTextView =
            view.findViewById(R.id.weather_condition_text_text_view)
        weatherConditionIconImageView =
            view.findViewById(R.id.weather_condition_icon_image_view)
        weatherMapImageView =
            view.findViewById(R.id.weather_map_image_view)

        addObservers()
    }

    private fun addObservers() {
        viewModel.showTopLoading.observe(viewLifecycleOwner) { shouldShow ->
            handleCurrentTempProgressLoader(shouldShow)
        }

        viewModel.showForecastLoading.observe(viewLifecycleOwner) { shouldShow ->
            handleDayForecastProgressLoader(shouldShow)
        }

        viewModel.showHourlyForecastLoading.observe(viewLifecycleOwner) { shouldShow ->
            handleHourlyForecastProgressLoader(shouldShow)
        }

        viewModel.currentWeather.observe(viewLifecycleOwner) { currentWeather ->
            displayCurrentWeatherInfo(currentWeather)

        }

        viewModel.fullWeather.observe(viewLifecycleOwner) { fiveDayForecastList ->
            updateFiveDayForecastList(fiveDayForecastList)
        }

        viewModel.hourlyForecast.observe(viewLifecycleOwner) { hourlyForecast ->
            updateHourlyForecastList(hourlyForecast)
        }
    }

    private fun updateHourlyForecastList(hourlyForecast: List<Hourly>?) {
        hourlForecastAdapter.updateList(hourlyForecast as MutableList<Hourly>)
        hourlyForecastRecyclerView?.adapter = hourlForecastAdapter

        popAdapter.updateList(hourlyForecast)
        popRecyclerView?.adapter = popAdapter
    }

    private fun updateFiveDayForecastList(fiveDayForecastList: List<Daily>) {
        dailyForecastAdapter.updateList(fiveDayForecastList)
        recyclerView?.adapter = dailyForecastAdapter
    }

    private fun displayCurrentWeatherInfo(currentWeather: CurrentWeather) {
        locationTextView.text = currentWeather.name
        tempTextView.text =
            getString(R.string.temperature_degrees, currentWeather.main.temp.substring(0, 2))
        weatherConditionDescTextView.text = currentWeather.weather[0].description

        Glide.with(requireContext())
            .load("https://openweathermap.org/img/wn/" + currentWeather.weather[0].icon + ".png")
            .into(weatherConditionIconImageView)

        Glide.with(requireContext())
            .load("https://tile.openweathermap.org/map/temp_new/5/10/10.png?appid=" + BuildConfig.API_KEY)
            .into(weatherMapImageView)
    }

    private fun handleHourlyForecastProgressLoader(shouldShow: Boolean) {
        if (shouldShow) {
            hourlyForecastProgressBar.visibility = View.VISIBLE
            hourlyForecastRecyclerView?.visibility = View.GONE
            popRecyclerView?.visibility = View.GONE
        } else {
            hourlyForecastProgressBar.visibility = View.GONE
            hourlyForecastRecyclerView?.visibility = View.VISIBLE
            popRecyclerView?.visibility = View.VISIBLE
        }
    }

    private fun handleCurrentTempProgressLoader(shouldShow: Boolean) {
        if (shouldShow) {
            topViewProgressBar.visibility = View.VISIBLE
            currentWeatherView.visibility = View.GONE
        } else {
            topViewProgressBar.visibility = View.GONE
            currentWeatherView.visibility = View.VISIBLE
        }
    }

    private fun handleDayForecastProgressLoader(shouldShow: Boolean) {
        if (shouldShow) {
            dailyForecastProgressBar.visibility = View.VISIBLE
            recyclerView?.visibility = View.GONE
        } else {
            dailyForecastProgressBar.visibility = View.GONE
            recyclerView?.visibility = View.VISIBLE
        }
    }


    private fun handlePermissions(view: View) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_DENIED
        ) {

            val locationPermissionRequest = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                        lifecycleScope.launch {
                            viewModel.getCurrentWeather()
                            viewModel.getFullWeather()
                            viewModel.getHourlyForecast()
                        }
                    }
                    else -> {
                        Navigation.findNavController(view)
                            .navigate(R.id.action_homeFragment_to_permissionsErrorFragment)
                    }
                }
            }

            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )

        } else if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_DENIED
        ) {

            Navigation.findNavController(view)
                .navigate(R.id.action_homeFragment_to_permissionsErrorFragment)
        } else if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            lifecycleScope.launch {
                viewModel.getCurrentWeather()
                viewModel.getFullWeather()
                viewModel.getHourlyForecast()
            }
        }
    }
}