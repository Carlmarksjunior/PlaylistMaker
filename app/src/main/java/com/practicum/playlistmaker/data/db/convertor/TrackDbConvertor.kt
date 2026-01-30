package com.practicum.playlistmaker.data.db.convertor

import com.practicum.playlistmaker.data.db.entity.TrackEntity
import com.practicum.playlistmaker.domain.search.model.Track

class TrackDbConvertor {

    fun map(track: Track): TrackEntity{
        return TrackEntity(
            track.trackId.toString(),
            track.artworkUrl100,
            track.trackName,
            track.artistName,
            track.collectionName,
            track.releaseYear,
            track.primaryGenreName,
            track.country,
            track.duration,
            track.previewUrl,
        )
    }

    fun map(trackEntity: TrackEntity): Track{
        return Track(
            trackEntity.trackName,
            trackEntity.artistName,
            trackEntity.duration,
            trackEntity.artworkUrl100,
            trackEntity.previewUrl,
            trackEntity.trackId.toInt(),
            trackEntity.country,
            trackEntity.primaryGenreName,
            trackEntity.collectionName,
            trackEntity.releaseYear,
            isFavorite = true
        )
    }
}