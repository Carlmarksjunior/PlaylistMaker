package com.practicum.playlistmaker.presentation.media.view_model.state

import androidx.annotation.StringRes
import com.practicum.playlistmaker.domain.search.model.Track

sealed class FavoriteTracksState {

    class Content(val tracks:List<Track>): FavoriteTracksState()
    class Empty(@StringRes val message: Int): FavoriteTracksState()
}