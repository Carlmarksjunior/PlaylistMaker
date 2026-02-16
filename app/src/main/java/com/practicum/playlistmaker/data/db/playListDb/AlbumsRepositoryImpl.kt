package com.practicum.playlistmaker.data.db.playListDb

import com.practicum.playlistmaker.data.db.playListDb.convertor.AlbumDbConvertor
import com.practicum.playlistmaker.data.db.playListDb.dataBase.AppDataBaseAlbum
import com.practicum.playlistmaker.data.db.tracksInAlbumDb.convertor.TrackInAlbumDbConvertor
import com.practicum.playlistmaker.data.db.tracksInAlbumDb.database.AppDataBaseTrackInAlbum
import com.practicum.playlistmaker.domain.db.playLists.AlbumsRepository
import com.practicum.playlistmaker.domain.db.playLists.model.Album
import com.practicum.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AlbumsRepositoryImpl(private val appDataBaseAlbum: AppDataBaseAlbum,
    private val albumDbConvertor: AlbumDbConvertor,
    private val appDataBaseTrackInAlbum: AppDataBaseTrackInAlbum,
    private val trackInAlbumDbConvertor: TrackInAlbumDbConvertor): AlbumsRepository {
    override suspend fun insertAlbum(album: Album) {
        appDataBaseAlbum.albumDao().insertAlbum(albumDbConvertor.map(album))
    }

    override suspend fun deleteAlbum(album: Album) {
        appDataBaseAlbum.albumDao().deleteAlbum(albumDbConvertor.map(album))
    }

    override fun getAllAlbums(): Flow<List<Album>> = flow {
        val albums = appDataBaseAlbum.albumDao().getAllAlbums()
        emit(albums.map { albumDbConvertor.map(it) })
    }

    override suspend fun updatesTracksInfoInAlbum(albumId: Int, tracksIds: String, tracksCount: Int) {
        appDataBaseAlbum.albumDao().updateTracksInfoInAlbum(albumId,tracksIds,tracksCount)
    }


    override suspend fun isAlbumExists(albumName: String?): Boolean {
        return appDataBaseAlbum.albumDao().isAlbumExists(albumName)
    }

    override suspend fun insertTrackInAlbum(track: Track) {
        appDataBaseTrackInAlbum.trackInAlbumDao().insertTrackInAlbum(trackInAlbumDbConvertor.map(track))
    }

}