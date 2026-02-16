package com.practicum.playlistmaker.data.db.favouritesTracksDb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.db.favouritesTracksDb.entity.TrackEntity


@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(trackEntity: TrackEntity)

    @Query("SELECT * FROM track_table")
    suspend fun getAllTracks(): List<TrackEntity>

    @Query("SELECT Id FROM track_table")
    suspend fun getAllIdTracks():List<String>

}