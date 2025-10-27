package com.practicum.playlistmaker.domain.api.interactor

import com.practicum.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)
    interface TracksConsumer {
        fun consume(foundTracks: List<Track>, isNetworkError : Boolean)
    }
}