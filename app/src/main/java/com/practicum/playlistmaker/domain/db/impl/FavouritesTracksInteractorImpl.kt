package com.practicum.playlistmaker.domain.db.impl

import com.practicum.playlistmaker.domain.db.FavouritesTracksInteractor
import com.practicum.playlistmaker.domain.db.FavouritesTracksRepository
import com.practicum.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavouritesTracksInteractorImpl(private val favouritesTracksRepository: FavouritesTracksRepository):
    FavouritesTracksInteractor {
    override suspend fun insertTrack(track: Track) {
        favouritesTracksRepository.insertTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        favouritesTracksRepository.deleteTrack(track)
    }

    override fun getAllTracks(): Flow<List<Track>> {
        return favouritesTracksRepository.getAllTracks().map { it.reversed() }
    }
}