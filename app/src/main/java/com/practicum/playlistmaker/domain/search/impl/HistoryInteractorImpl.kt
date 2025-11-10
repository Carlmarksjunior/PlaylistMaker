package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.domain.search.HistoryInteractor
import com.practicum.playlistmaker.domain.search.HistoryRepository
import com.practicum.playlistmaker.domain.search.model.Track

class HistoryInteractorImpl(private val historyRepository: HistoryRepository): HistoryInteractor {
    override fun getSaveTracks(): List<Track> {
        return historyRepository.getSaveTracks()
    }

    override fun saveTrack(track: Track) {
        historyRepository.saveTrack(track)
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }


}