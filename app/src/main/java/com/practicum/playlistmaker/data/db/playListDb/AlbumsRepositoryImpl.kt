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

    override suspend fun deleteAlbumById(albumId: Int){
        appDataBaseAlbum.albumDao().deleteAlbumById(albumId = albumId)
    }

    override fun getAllAlbums(): Flow<List<Album>> = flow {
        val albums = appDataBaseAlbum.albumDao().getAllAlbums()
        emit(albums.map { albumDbConvertor.map(it) })
    }

    override fun getAlbumFromId(albumId: Int): Flow<Album> = flow {
        val album = albumDbConvertor.map(appDataBaseAlbum.albumDao().getAlbumFromId(albumId))
        emit(album)
    }

    override suspend fun updatesTracksInfoInAlbum(albumId: Int, tracksIds: String, tracksCount: Int) {
        appDataBaseAlbum.albumDao().updateTracksInfoInAlbum(albumId,tracksIds,tracksCount)
    }

    override suspend fun updateAlbumFromId(
        albumId: Int,
        albumName: String,
        albumDescription: String,
        albumPathImage: String
    ) {
        appDataBaseAlbum.albumDao().updateAlbumFromId(albumId,albumName,albumDescription,albumPathImage)
    }


    override suspend fun isAlbumExists(albumName: String?): Boolean {
        return appDataBaseAlbum.albumDao().isAlbumExists(albumName)
    }

    override suspend fun insertTrackInAlbum(track: Track) {
        appDataBaseTrackInAlbum.trackInAlbumDao().insertTrackInAlbum(trackInAlbumDbConvertor.map(track))
    }

    override fun getTracksByIds(trackIds: List<String>): Flow<List<Track>> = flow{
        val tracks = appDataBaseTrackInAlbum.trackInAlbumDao().getTracksByIds(trackIds)
        emit(tracks.map { trackInAlbumDbConvertor.map(it) })
    }

    override suspend fun deleteTrackById(trackId: Int) {
        appDataBaseTrackInAlbum.trackInAlbumDao().deleteTrackById(trackId)
    }

}