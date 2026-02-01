package com.practicum.playlistmaker.presentation.search.view_model.state

import com.practicum.playlistmaker.domain.search.model.Track

sealed interface TrackState  {
    object Loading : TrackState
    data class Content(
        val tracks: List<Track>
    ) : TrackState

    data class History(
        val tracks: List<Track>
    ) : TrackState

    data class Error(
        val errorMessage: String
    ) : TrackState

    data class Empty(
        val message: String
    ) : TrackState
}