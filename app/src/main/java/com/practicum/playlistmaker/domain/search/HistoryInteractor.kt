package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.search.model.Track

interface HistoryInteractor {
    suspend fun getSaveTracks(): List<Track>

    fun saveTrack(track: Track)

    fun clearHistory()
}