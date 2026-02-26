package com.practicum.playlistmaker.ui.media.fragments.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemPlaylistBinding
import com.practicum.playlistmaker.domain.db.playLists.model.Album
import java.io.File

class AlbumsViewHolder(private val binding: ItemPlaylistBinding,
                       private val context: Context?,
                       private val clickListener:(Album)-> Unit): RecyclerView.ViewHolder(binding.root) {

    fun bind(album: Album){

        binding.apply {
            val radius = root.context.resources.getDimensionPixelSize(R.dimen.marginTop8)
            Glide.with(binding.root)
                .load(File(album.pathImage?:""))
                .placeholder(R.drawable.ic_placeholder_160)
                .transform(RoundedCorners(radius))
                .centerCrop()
                .into(binding.imageViewItemAlbum)
            tvNameItemAlbum.text = album.albumName
            tvTracksItemAlbum.text = getPluralTracks(album.tracksCount)
            root.setOnClickListener {
                clickListener(album)
            }
        }

    }

    private fun getPluralTracks(count: Int): String {
        return context?.resources!!.getQuantityString(R.plurals.tracks_count,count,count)
    }

}