package com.practicum.playlistmaker.ui.player.fragment.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemAlbumBottomSheetBinding
import com.practicum.playlistmaker.domain.db.playLists.model.Album
import java.io.File

class AlbumsViewHolderBottomSheet(private val binding: ItemAlbumBottomSheetBinding,
                                  private val context: Context?): RecyclerView.ViewHolder(binding.root) {

    fun bind(album: Album,clickListener:(Album)-> Unit){
        binding.apply {
            val radius = root.context.resources.getDimensionPixelSize(R.dimen.marginTop8)
            Glide.with(binding.root)
                .load(File(album.pathImage?:""))
                .placeholder(R.drawable.ic_placeholder_160)
                .transform(RoundedCorners(radius))
                .centerCrop()
                .into(binding.artAlbum)
            tracksCountTv.text = getPluralTracks(album.tracksCount)
            albumNameTv.text = album.albumName
            binding.root.setOnClickListener {
                clickListener(album)
            }
        }


    }
    private fun getPluralTracks(count: Int): String {
        return context?.resources!!.getQuantityString(R.plurals.tracks_count,count,count)
    }
}