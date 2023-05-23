package com.globalkinetics.android.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.RecyclerView
import com.globalkinetics.android.R
import com.globalkinetics.android.model.Daily
import com.globalkinetics.android.model.Hourly
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class HourlForecastAdapter @Inject constructor() :
    RecyclerView.Adapter<HourlForecastAdapter.ViewHolder>() {
    private var hourlyForecastList = mutableListOf<Hourly>()

    fun updateList(list: MutableList<Hourly>) {
        this.hourlyForecastList = list
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): HourlForecastAdapter.ViewHolder {
        val rootView = getLayoutInflater(viewGroup.context).inflate(R.layout.hourly_forecast_item, viewGroup, false)
        return ViewHolder(rootView)
    }

    override fun getItemCount(): Int = hourlyForecastList.size

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hour = hourlyForecastList[position]

        val date = Date(hour.dt.times(1_000))
        val sdf = SimpleDateFormat("HH a")
        sdf.timeZone = TimeZone.getTimeZone("GTM+2")
        val formattedDate = sdf.format(date.time)

        holder.dayDesc.text = formattedDate
    }

    @VisibleForTesting
    internal fun getLayoutInflater(context: Context): LayoutInflater {
        return LayoutInflater.from(context)
    }

    inner class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        internal var dayDesc: TextView = itemView.findViewById(R.id.hour_text_view)
    }
}