package com.practicum.playlistmaker.data.db.tracksInAlbumDb.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.data.db.tracksInAlbumDb.dao.TrackInAlbumDao
import com.practicum.playlistmaker.data.db.tracksInAlbumDb.entity.TrackInAlbumEntity


@Database(version = 1, entities = [TrackInAlbumEntity::class])
abstract class AppDataBaseTrackInAlbum: RoomDatabase() {
    abstract fun trackInAlbumDao(): TrackInAlbumDao
}