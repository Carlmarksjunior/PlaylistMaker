package com.practicum.playlistmaker.ui.player.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.ItemAlbumBottomSheetBinding
import com.practicum.playlistmaker.domain.db.playLists.model.Album

class AlbumsBottomSheetAdapter(private val albums: List<Album>,val clickListener: (Album)-> Unit): RecyclerView.Adapter<AlbumsViewHolderBottomSheet>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumsViewHolderBottomSheet {
        val view = ItemAlbumBottomSheetBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AlbumsViewHolderBottomSheet(view)
    }

    override fun onBindViewHolder(
        holder: AlbumsViewHolderBottomSheet,
        position: Int
    ) {
        holder.bind(albums[position],clickListener)
    }

    override fun getItemCount(): Int = albums.size
}