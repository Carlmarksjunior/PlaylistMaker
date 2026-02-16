package com.practicum.playlistmaker.presentation.media.view_model.state

import com.practicum.playlistmaker.domain.db.playLists.model.Album

sealed class PlayListState {

    class Content(val albums:List<Album>): PlayListState()
    class Empty(val message: String): PlayListState()
}