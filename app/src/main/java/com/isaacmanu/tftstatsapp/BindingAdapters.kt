package com.isaacmanu.tftstatsapp

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.isaacmanu.tftstatsapp.adapter.CardAdapter
import com.isaacmanu.tftstatsapp.network.model.RankedData
import com.isaacmanu.tftstatsapp.network.model.RecyclerViewItem
import com.isaacmanu.tftstatsapp.overview.RiotApiStatus
import kotlin.time.Duration.Companion.seconds

//List Adapter setup
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<RecyclerViewItem>?) {
    val adapter = recyclerView.adapter as CardAdapter
    adapter.submitList(data)

}

@BindingAdapter("placement")
fun bindPlacement(textView: TextView, matchData: RecyclerViewItem) {
    val puuId = matchData.puuid
    val placementData = matchData.matchData.info.participants
    placementData.forEach{

        if (puuId == it.puuid) {
            val placementText = it.placement
            textView.text = "Placement: $placementText" //This is bad practice, should be string reference
        }
    }

}

/*
Coil library is used to load an image off the web using data gathered from our api requests.
Currently using a publicly available CDN, would be better to create and use our own.
*/
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, profileIconId: Int) {
    val profileIconIdString = profileIconId.toString()
    val imageUrl = "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/profile-icons/$profileIconIdString.jpg" //Placeholder URL
    val imageUri = imageUrl.toUri()
    imgView.scaleType = ImageView.ScaleType.CENTER_CROP
    imgView.load(imageUri) {
        placeholder(R.drawable.ic_baseline_broken_image_24)
    }

}


@BindingAdapter("timeInSeconds")
fun bindTimeInSeconds(txtView: TextView, time: Double) {
    txtView.text = time.seconds.inWholeSeconds.seconds.toString()
}

/*
SearchableActivity uses this display the ranked information of the user by combining multiple
values in the viewmodel
*/
@BindingAdapter("rankedData")
fun bindRankedData(textView: TextView, rankedData: RankedData?) {
    if (rankedData != null) {
        textView.isVisible = true
        textView.text = "${rankedData.tier} ${rankedData.rank}, ${rankedData.leaguePoints} LP"
    } else {
        textView.isVisible = false
    }

}

//Each ListAdapter item uses this to determine the correct queue type to display.
@BindingAdapter("queueType")
fun bindQueueType(textView: TextView, type: String) {
    when (type) {
        "standard" -> {
            textView.text = "Ranked" // These should all be string references
        }
        "pairs" -> {
            textView.text = "Double-Up"
        }
        else -> {
            textView.text = "Normal"
        }
    }
}

/*
This is taken directly from the Google hosted tutorial series titled 'Android Basics in Kotlin'
 */
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