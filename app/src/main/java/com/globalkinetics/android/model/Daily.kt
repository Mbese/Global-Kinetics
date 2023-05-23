package com.globalkinetics.android.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


data class Daily(

    @SerializedName("dt") var dt: Long,
    @SerializedName("sunrise") var sunrise: Int? = null,
    @SerializedName("sunset") var sunset: Int? = null,
    @SerializedName("moonrise") var moonrise: Int? = null,
    @SerializedName("moonset") var moonset: Int? = null,
    @SerializedName("moon_phase") var moonPhase: Double? = null,
    @SerializedName("temp") var temp: Temp? = Temp(),
    @SerializedName("feels_like") var feelsLike: FeelsLike? = FeelsLike(),
    @SerializedName("pressure") var pressure: Int? = null,
    @SerializedName("humidity") var humidity: Int? = null,
    @SerializedName("dew_point") var dewPoint: Double? = null,
    @SerializedName("wind_speed") var windSpeed: Double? = null,
    @SerializedName("wind_deg") var windDeg: Int? = null,
    @SerializedName("wind_gust") var windGust: Double? = null,
    @SerializedName("weather") var weather: ArrayList<Weather> = arrayListOf(),
    @SerializedName("clouds") var clouds: Int? = null,
    @SerializedName("pop") var pop: Double? = null,
    @SerializedName("rain") var rain: Double? = null,
    @SerializedName("uvi") var uvi: Double? = null

)