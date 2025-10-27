package com.practicum.playlistmaker.domain.api.repository

import com.practicum.playlistmaker.domain.models.Track

interface HistoryRepository {
    fun getSaveTracks():List<Track>
    fun saveTrack(track: Track)
    fun clearHistory()
}