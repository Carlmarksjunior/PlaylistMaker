package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.data.db.database.AppDataBase
import com.practicum.playlistmaker.data.preferences.SharedPreferenceManager
import com.practicum.playlistmaker.domain.search.HistoryRepository
import com.practicum.playlistmaker.domain.search.model.Track

class HistoryRepositoryImpl(private val sharedPreferenceManager: SharedPreferenceManager,
    private val appDataBase: AppDataBase
):HistoryRepository {
    override suspend fun getSaveTracks(): List<Track> {
        val trackIds: List<String> = appDataBase.trackDao().getAllIdTracks()
        val tracks: List<Track> = sharedPreferenceManager.getSaveTracks()
        return tracks.map { Track(it.trackName,
            it.artistName,
            it.duration,
            it.artworkUrl100,
            it.previewUrl,
            it.trackId,
            it.country,
            it.primaryGenreName,
            it.collectionName,
            it.releaseYear,
            it.isFavorite == trackIds.contains(it.trackId.toString()))}

    }

    override fun saveTrack(track: Track) {
        sharedPreferenceManager.saveTrack(track)
    }

    override fun clearHistory() {
        sharedPreferenceManager.clearHistory()
    }

}