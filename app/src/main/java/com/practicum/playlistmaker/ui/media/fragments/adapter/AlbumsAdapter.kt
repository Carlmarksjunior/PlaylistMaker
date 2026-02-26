package com.practicum.playlistmaker.ui.media.fragments.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.ItemPlaylistBinding
import com.practicum.playlistmaker.domain.db.playLists.model.Album

class AlbumsAdapter(private val albums: List<Album>,
                    private val context: Context?,
                    private val clickListener:(album: Album)-> Unit): RecyclerView.Adapter<AlbumsViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumsViewHolder {
        val binding = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AlbumsViewHolder(binding,context,clickListener)
    }

    override fun onBindViewHolder(
        holder: AlbumsViewHolder,
        position: Int
    ) {
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int = albums.size


}