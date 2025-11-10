package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.data.preferences.SharedPreferenceManager
import com.practicum.playlistmaker.domain.search.HistoryRepository
import com.practicum.playlistmaker.domain.search.model.Track

class HistoryRepositoryImpl(private val sharedPreferenceManager: SharedPreferenceManager):
    HistoryRepository {
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