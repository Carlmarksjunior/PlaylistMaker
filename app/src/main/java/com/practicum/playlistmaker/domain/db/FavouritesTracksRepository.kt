package com.practicum.playlistmaker.domain.db

import com.practicum.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouritesTracksRepository {
    suspend fun insertTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    fun getAllTracks(): Flow<List<Track>>
}