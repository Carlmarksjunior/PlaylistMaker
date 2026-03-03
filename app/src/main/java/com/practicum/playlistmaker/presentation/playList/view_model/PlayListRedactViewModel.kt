package com.practicum.playlistmaker.presentation.playList.view_model

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.db.playLists.AlbumInteractor
import com.practicum.playlistmaker.presentation.playList.state.PlayListCreateState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayListRedactViewModel(
    private val albumInteractor: AlbumInteractor,
    private val context: Context
) : PlayListCreateViewModel(albumInteractor, context) {

    fun getAlbumFromId(albumId: Int?) {
        if (albumId != null) {
            viewModelScope.launch(Dispatchers.IO) {
                albumInteractor.getAlbumFromId(albumId).collect {
                    albumLiveData.postValue(
                        PlayListCreateState(
                            albumName = it.albumName,
                            albumDescription = it.albumDescription,
                            albumImagePath = it.pathImage
                        )
                    )
                }

            }
        }

    }

    fun updateAlbum(
        albumId: Int,
        alumName: String,
        albumDescription: String,
        albumPathImage: String
    ) {
        viewModelScope.launch {
            albumInteractor.updateAlbumFromId(albumId, alumName, albumDescription, albumPathImage)
        }
    }


}