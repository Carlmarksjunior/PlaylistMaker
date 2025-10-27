package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.interactor.TracksInteractor
import com.practicum.playlistmaker.domain.api.repository.TracksRepository
import java.io.IOException
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            try {
                val tracks = repository.searchTracks(expression)
                consumer.consume(tracks, false)
            } catch (e: IOException) {
                consumer.consume(emptyList(), true)
            }
        }
    }
}