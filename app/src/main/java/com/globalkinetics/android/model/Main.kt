package com.globalkinetics.android.model

import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

data class Main(
    @SerializedName("temp")
    val temp: String,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("pressure")
    val pressure: Int,
    @SerializedName("humidity")
    val humidity: Int
) {
}