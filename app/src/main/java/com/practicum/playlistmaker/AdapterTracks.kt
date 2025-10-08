package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AdapterTracks(private val tracks: MutableList<Track>,

                    val clickListener: (Track) -> Unit): RecyclerView.Adapter<TrackViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newTracks : List<Track>){
        tracks.clear()
        tracks.addAll(newTracks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position], clickListener)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}