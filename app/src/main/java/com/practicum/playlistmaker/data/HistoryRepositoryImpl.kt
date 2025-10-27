package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.preference.SharedPreferenceManager
import com.practicum.playlistmaker.domain.api.repository.HistoryRepository
import com.practicum.playlistmaker.domain.models.Track

class HistoryRepositoryImpl(private val sharedPreferenceManager: SharedPreferenceManager): HistoryRepository {
    override fun getSaveTracks(): List<Track> {
        return sharedPreferenceManager.getSaveTracks()
    }

    override fun saveTrack(track: Track) {
        sharedPreferenceManager.saveTrack(track)
    }

    override fun clearHistory() {
        sharedPreferenceManager.clearHistory()
    }

}