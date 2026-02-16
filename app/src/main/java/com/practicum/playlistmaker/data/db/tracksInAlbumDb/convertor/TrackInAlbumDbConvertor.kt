package com.practicum.playlistmaker.data.db.tracksInAlbumDb.convertor

import com.practicum.playlistmaker.data.db.tracksInAlbumDb.entity.TrackInAlbumEntity
import com.practicum.playlistmaker.domain.search.model.Track

class TrackInAlbumDbConvertor {

    fun map(track: Track): TrackInAlbumEntity {
        return TrackInAlbumEntity(
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

    fun map(trackInAlbumEntity: TrackInAlbumEntity): Track{
        return Track(
            trackInAlbumEntity.trackName,
            trackInAlbumEntity.artistName,
            trackInAlbumEntity.duration,
            trackInAlbumEntity.artworkUrl100,
            trackInAlbumEntity.previewUrl,
            trackInAlbumEntity.trackId.toInt(),
            trackInAlbumEntity.country,
            trackInAlbumEntity.primaryGenreName,
            trackInAlbumEntity.collectionName,
            trackInAlbumEntity.releaseYear,
            isFavorite = true
        )
    }
}