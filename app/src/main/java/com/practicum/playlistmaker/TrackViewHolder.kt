package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    private val artSong : ImageView = itemView.findViewById<ImageView>(R.id.artSong)
    private val trackName: TextView = itemView.findViewById<TextView>(R.id.trackName)
    private val artistName: TextView = itemView.findViewById<TextView>(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById<TextView>(R.id.trackTime)

    constructor(parent: ViewGroup):this(LayoutInflater.from(parent.context)
        .inflate(R.layout.activity_track,parent,false))

    fun bind (track: Track){
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(artSong)
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.trackTime
    }
}