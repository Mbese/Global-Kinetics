package com.globalkinetics.android.view

import android.view.View
import android.widget.BaseAdapter
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.globalkinetics.android.model.Daily


@BindingAdapter("manageState")
fun manageState(progressBar: ProgressBar, state: Boolean) {
    progressBar.visibility = if (state) View.VISIBLE else View.GONE
}

//@BindingAdapter("setImage")
//fun setImage(imageView: ImageView, imageUrl: String) {
//    Picasso.with(imageView.context)
//        .load(imageUrl)
//        .into(imageView)
//}