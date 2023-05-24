package com.globalkinetics.android.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.globalkinetics.android.R
import com.globalkinetics.android.model.Daily
import java.text.SimpleDateFormat
import java.util.*

class DailyForecastAdapter(private val context: Context) :  RecyclerView.Adapter<DailyForecastAdapter.ViewHolder>() {
    private var fiveDayForecastList = mutableListOf<Daily>()

    fun updateList(list : List<Daily>) {
        fiveDayForecastList.addAll(list)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val rootView = getLayoutInflater(viewGroup.context).inflate(R.layout.day_forecast_item, viewGroup, false)
        return ViewHolder(rootView)
    }

    override fun getItemCount(): Int = fiveDayForecastList.size

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayForecast = fiveDayForecastList[position]

        val weatherIconImageView = holder.weatherIconImageView
        val url = "https://openweathermap.org/img/wn/"+dayForecast.weather[0].icon+".png"
        Glide.with(context)
            .load(url)
            .into(weatherIconImageView)

        val day = SimpleDateFormat("EEEE").format(Date(dayForecast.dt * 1_000))

        holder.dayDesc.text = day
        holder.tempDescTextView.text = dayForecast.weather[0].description

        val minTemp = dayForecast.temp?.min.toString().substring(0, 2)
        val maxTemp = dayForecast.temp?.max.toString().substring(0, 2)
        holder.dayTempTextView.text = context.getString(R.string.min_and_max_temperature_degrees, minTemp, maxTemp)
    }

    @VisibleForTesting
    internal fun getLayoutInflater(context: Context): LayoutInflater {
        return LayoutInflater.from(context)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var weatherIconImageView: ImageView = itemView.findViewById(R.id.weather_condition_icon_image_view)
        internal var dayDesc: TextView = itemView.findViewById(R.id.day_description_text_view)
        internal var tempDescTextView: TextView = itemView.findViewById(R.id.temp_description_text_view)
        internal var dayTempTextView: TextView = itemView.findViewById(R.id.day_temp_text_view)
    }
}