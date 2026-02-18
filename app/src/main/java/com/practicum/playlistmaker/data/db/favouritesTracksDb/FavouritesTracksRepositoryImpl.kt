package com.practicum.playlistmaker.data.db.favouritesTracksDb

import com.practicum.playlistmaker.data.db.favouritesTracksDb.convertor.TrackDbConvertor
import com.practicum.playlistmaker.data.db.favouritesTracksDb.database.AppDataBase
import com.practicum.playlistmaker.domain.db.favouritesTracks.FavouritesTracksRepository
import com.practicum.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouritesTracksRepositoryImpl(private val appDataBase: AppDataBase,
                                     private val trackDbConvertor: TrackDbConvertor
): FavouritesTracksRepository {
    override suspend fun insertTrack(track: Track) {
        appDataBase.trackDao().insertTrack(trackDbConvertor.map(track))
    }

    override suspend fun deleteTrack(track: Track) {
        appDataBase.trackDao().deleteTrack(trackDbConvertor.map(track))
    }

    override fun getAllTracks(): Flow<List<Track>> = flow {
        val tracks = appDataBase.trackDao().getAllTracks()

        emit(tracks.map { trackDbConvertor.map(it) })
    }


}