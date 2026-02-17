package com.practicum.playlistmaker.domain.db.playLists

import com.practicum.playlistmaker.domain.db.playLists.model.Album
import com.practicum.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface AlbumInteractor {
    suspend fun insertAlbum(album: Album)
    suspend fun deleteAlbumsById(albumId: Int)
    fun getAllAlbums(): Flow<List<Album>>
    suspend fun updateTracksInfoInAlbum(albumId: Int, tracksIds: String, tracksCount: Int)

    suspend fun isAlbumExists(albumName: String?): Boolean

    suspend fun insertTrackInAlbum(track: Track)

}