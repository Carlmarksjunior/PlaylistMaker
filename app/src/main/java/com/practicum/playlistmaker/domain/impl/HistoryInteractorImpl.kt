package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.interactor.HistoryInteractor
import com.practicum.playlistmaker.domain.api.repository.HistoryRepository
import com.practicum.playlistmaker.domain.models.Track

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