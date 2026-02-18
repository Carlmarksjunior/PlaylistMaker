package com.practicum.playlistmaker.domain.db.playLists

import com.practicum.playlistmaker.domain.db.playLists.model.Album
import com.practicum.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface AlbumsRepository {
    suspend fun insertAlbum(album: Album)
    suspend fun deleteAlbumById(albumId: Int)
    fun getAllAlbums(): Flow<List<Album>>

    suspend fun updatesTracksInfoInAlbum(albumId: Int, tracksIds: String, tracksCount: Int)

    suspend fun isAlbumExists(albumName: String?):Boolean

    suspend fun insertTrackInAlbum(track: Track)
}