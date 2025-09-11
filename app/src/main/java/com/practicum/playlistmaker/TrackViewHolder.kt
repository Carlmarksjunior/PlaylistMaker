package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TrackViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_track, parent, false)
    ) {
    private val artSong: ImageView = itemView.findViewById<ImageView>(R.id.artSong)
    private val trackName: TextView = itemView.findViewById<TextView>(R.id.trackName)
    private val artistName: TextView = itemView.findViewById<TextView>(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById<TextView>(R.id.trackTime)


    fun bind(track: Track, searchHistory: SearchHistory) {
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(artSong)
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(
            Date(track.trackTimeMillis)
        )
        itemView.setOnClickListener {
            searchHistory.saveTrack(track)
        }
    }
}