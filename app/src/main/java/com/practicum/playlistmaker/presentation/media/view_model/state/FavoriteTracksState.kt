package com.practicum.playlistmaker.presentation.media.view_model.state

import com.practicum.playlistmaker.domain.search.model.Track

sealed class FavoriteTracksState {

    class Content(val tracks:List<Track>): FavoriteTracksState()
    class Empty(val message: String): FavoriteTracksState()
}