package com.practicum.playlistmaker.ui.search.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemTrackBinding
import com.practicum.playlistmaker.domain.search.model.Track

class TrackViewHolder(private val binding: ItemTrackBinding) :
    RecyclerView.ViewHolder(
        binding.root) {

    companion object {
        fun from(parent: ViewGroup): TrackViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTrackBinding.inflate(inflater, parent, false)
            return TrackViewHolder(binding)
        }
    }

    private val radiusPx = dpToPx(10)

    fun bind(track: Track, clickListener: (Track) -> Unit) {
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(radiusPx))
            .into(binding.artSong)
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTime.text = track.duration
        itemView.setOnClickListener {
            clickListener(track)

        }
    }
    fun RecyclerView.ViewHolder.dpToPx(dp: Int): Int {
        return (dp * itemView.context.resources.displayMetrics.density).toInt()
    }

}