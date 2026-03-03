package com.practicum.playlistmaker.ui.search.fragment

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.search.model.Track

class AdapterTracks(val clickListener: (Track) -> Unit): RecyclerView.Adapter<TrackViewHolder>() {
    val trackList = ArrayList<Track>()

    val searchHistory: ArrayList<Track> = ArrayList<Track>()

    var longListener:((Track)-> Unit) = {}

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newTracks : List<Track>){
        trackList.clear()
        trackList.addAll(newTracks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder.Companion.from(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position], clickListener,longListener)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}