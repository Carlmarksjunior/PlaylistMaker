package com.practicum.playlistmaker.data.db.playListDb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album_table")
data class AlbumEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "Name")
    val albumName: String?,
    @ColumnInfo(name = "Description")
    val albumDescription: String?,
    @ColumnInfo(name = "Path_image")
    val pathImage: String?,
    @ColumnInfo(name = "Tracks_id")
    val tracksIds: String = "[]",
    @ColumnInfo(name = "count_tracks")
    val tracksCount: Int = 0
)