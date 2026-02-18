package com.practicum.playlistmaker.data.db.tracksInAlbumDb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.practicum.playlistmaker.data.db.tracksInAlbumDb.entity.TrackInAlbumEntity


@Dao
interface TrackInAlbumDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackInAlbum(track: TrackInAlbumEntity)

}