package com.practicum.playlistmaker.data.db.playListDb.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.data.db.playListDb.dao.AlbumDao
import com.practicum.playlistmaker.data.db.playListDb.entity.AlbumEntity

@Database(version = 1, entities = [AlbumEntity::class])
abstract class AppDataBaseAlbum: RoomDatabase() {
    abstract fun albumDao(): AlbumDao
}