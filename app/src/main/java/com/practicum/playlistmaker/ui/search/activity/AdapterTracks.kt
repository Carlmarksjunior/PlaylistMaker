package com.practicum.playlistmaker.ui.search.activity

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.search.model.Track

class AdapterTracks(val clickListener: (Track) -> Unit): RecyclerView.Adapter<TrackViewHolder>() {
    val trackList = ArrayList<Track>()

    val searchHistory: ArrayList<Track> = ArrayList<Track>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newTracks : List<Track>){
        trackList.clear()
        trackList.addAll(newTracks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder=
        TrackViewHolder.from(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}