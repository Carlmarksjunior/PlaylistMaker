package com.practicum.playlistmaker.data.db.favouritesTracksDb.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.data.db.favouritesTracksDb.dao.TrackDao
import com.practicum.playlistmaker.data.db.favouritesTracksDb.entity.TrackEntity


@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDataBase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
}