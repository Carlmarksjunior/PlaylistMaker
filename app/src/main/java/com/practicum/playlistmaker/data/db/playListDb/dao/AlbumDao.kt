package com.practicum.playlistmaker.data.db.playListDb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.db.playListDb.entity.AlbumEntity

@Dao
interface AlbumDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(albumEntity: AlbumEntity)

    @Query("DELETE from album_table WHERE id= :albumId")
    suspend fun deleteAlbumById(albumId: Int)

    @Query("SELECT * FROM album_table")
    suspend fun getAllAlbums(): List<AlbumEntity>

    @Query("SELECT * FROM album_table WHERE id = :albumId")
    suspend fun getAlbumFromId(albumId: Int): AlbumEntity

    @Query("UPDATE album_table SET Tracks_id = :tracksIds, count_tracks = :tracksCount WHERE id = :albumId")
    suspend fun updateTracksInfoInAlbum(albumId: Int, tracksIds: String, tracksCount: Int)

    @Query("UPDATE album_table SET Name = :albumName, Description = :albumDescription,Path_image=:albumPathImage  WHERE id = :albumId")
    suspend fun updateAlbumFromId(albumId: Int,albumName: String,albumDescription: String,albumPathImage: String)


    @Query("SELECT EXISTS(SELECT 1 FROM album_table WHERE name = :albumName)")
    suspend fun isAlbumExists(albumName: String?): Boolean


}