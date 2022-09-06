package com.example.searchviewapp

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.searchviewapp.adapter.CardAdapter
import com.example.searchviewapp.network.model.MatchData
import com.example.searchviewapp.overview.RiotApiStatus

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<MatchData>?) {
    val adapter = recyclerView.adapter as CardAdapter?
    adapter?.submitList(data)

}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, srcUrl: String?) {
    srcUrl?.let {
        val imgUri = srcUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri) {
            placeholder(R.drawable.ic_baseline_broken_image_24) //Need placeholder drawable
            error(R.drawable.ic_baseline_wifi_off_24) //Need error drawable
        }
    }
}

@BindingAdapter("apiStatus")
fun bindStatus(statusTextView: TextView, status: RiotApiStatus?) {
    when (status) {
        RiotApiStatus.LOADING -> {
            statusTextView.visibility = View.VISIBLE
            statusTextView.text = "Loading..."
        }
        RiotApiStatus.ERROR -> {
            statusTextView.visibility = View.VISIBLE
            statusTextView.text = "Error: Api encountered an error"
        }
        RiotApiStatus.DONE -> {
            statusTextView.visibility = View.GONE
        }
        null -> {
            statusTextView.visibility = View.VISIBLE
            statusTextView.text = "Error: Status is null"
        }
    }
}