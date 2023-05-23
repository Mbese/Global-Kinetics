package com.globalkinetics.android.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.RecyclerView
import com.globalkinetics.android.R
import com.globalkinetics.android.model.Hourly
import javax.inject.Inject

class PopAdapter @Inject constructor() :
    RecyclerView.Adapter<PopAdapter.ViewHolder>() {
    private var popList = mutableListOf<Hourly>()

    fun updateList(list: MutableList<Hourly>) {
        this.popList = list
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): PopAdapter.ViewHolder {
        val rootView = getLayoutInflater(viewGroup.context).inflate(R.layout.pop_item, viewGroup, false)
        return ViewHolder(rootView)
    }

    override fun getItemCount(): Int = popList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hour = popList[position]

        holder.popTextView.text = hour.pop.toString()
    }

    @VisibleForTesting
    internal fun getLayoutInflater(context: Context): LayoutInflater {
        return LayoutInflater.from(context)
    }

    inner class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        internal var popTextView: TextView = itemView.findViewById(R.id.pop_text_view)
    }
}