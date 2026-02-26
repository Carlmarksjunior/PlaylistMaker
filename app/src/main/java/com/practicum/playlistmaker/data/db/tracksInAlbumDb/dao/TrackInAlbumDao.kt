package com.practicum.playlistmaker.data.db.tracksInAlbumDb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.db.tracksInAlbumDb.entity.TrackInAlbumEntity


@Dao
interface TrackInAlbumDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackInAlbum(track: TrackInAlbumEntity)

    @Query("SELECT * FROM track_in_album_table WHERE Id IN (:trackIds)")
    suspend fun getTracksByIds(trackIds: List<String>): List<TrackInAlbumEntity>

    @Query("DELETE FROM track_in_album_table WHERE id=:trackId")
    suspend fun deleteTrackById(trackId: Int)

}