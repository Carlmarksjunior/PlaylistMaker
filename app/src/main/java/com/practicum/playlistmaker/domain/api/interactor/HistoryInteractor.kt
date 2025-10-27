package com.practicum.playlistmaker.domain.api.interactor

import com.practicum.playlistmaker.domain.models.Track

interface HistoryInteractor {
    fun getSaveTracks(): List<Track>

    fun saveTrack(track: Track)

    fun clearHistory()
}