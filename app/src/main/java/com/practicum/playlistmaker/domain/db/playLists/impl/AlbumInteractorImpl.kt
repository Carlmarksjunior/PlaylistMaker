package com.practicum.playlistmaker.domain.db.playLists.impl

import com.practicum.playlistmaker.domain.db.playLists.AlbumInteractor
import com.practicum.playlistmaker.domain.db.playLists.AlbumsRepository
import com.practicum.playlistmaker.domain.db.playLists.model.Album
import com.practicum.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class AlbumInteractorImpl(private val albumsRepositoryImpl: AlbumsRepository): AlbumInteractor {
    override suspend fun insertAlbum(album: Album) {
        albumsRepositoryImpl.insertAlbum(album)
    }

    override suspend fun deleteAlbumsById(albumId: Int) {
        albumsRepositoryImpl.deleteAlbumById(albumId)
    }

    override fun getAllAlbums(): Flow<List<Album>> {
        return albumsRepositoryImpl.getAllAlbums()
    }

    override fun getAlbumFromId(albumId: Int): Flow<Album> {
        return albumsRepositoryImpl.getAlbumFromId(albumId)
    }

    override suspend fun updateTracksInfoInAlbum(
        albumId: Int,
        tracksIds: String,
        tracksCount: Int
    ) {
        albumsRepositoryImpl.updatesTracksInfoInAlbum(albumId, tracksIds,tracksCount)
    }

    override suspend fun updateAlbumFromId(
        albumId: Int,
        albumName: String,
        albumDescription: String,
        albumPathImage: String
    ) {
        albumsRepositoryImpl.updateAlbumFromId(albumId,albumName,albumDescription,albumPathImage)
    }


    override suspend fun isAlbumExists(albumName: String?): Boolean {
        return albumsRepositoryImpl.isAlbumExists(albumName)
    }

    override suspend fun insertTrackInAlbum(track: Track) {
        albumsRepositoryImpl.insertTrackInAlbum(track)
    }

    override fun getTracksByIds(trackIds: List<String>): Flow<List<Track>> {
        return albumsRepositoryImpl.getTracksByIds(trackIds)
    }

    override suspend fun deleteTrackById(trackId: Int) {
        albumsRepositoryImpl.deleteTrackById(trackId)
    }
}