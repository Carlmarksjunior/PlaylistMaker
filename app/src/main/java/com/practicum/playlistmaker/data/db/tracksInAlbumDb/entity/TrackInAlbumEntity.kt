package com.practicum.playlistmaker.data.db.tracksInAlbumDb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "track_in_album_table")
data class TrackInAlbumEntity(
    @PrimaryKey @ColumnInfo(name = "Id")
    val trackId: String,
    @ColumnInfo(name = "Art")
    val artworkUrl100: String?,
    @ColumnInfo(name = "Track_name")
    val trackName: String?,
    @ColumnInfo(name = "Artist_name")
    val artistName: String?,
    @ColumnInfo(name = "Collection_name")
    val collectionName: String?,
    @ColumnInfo(name = "Date")
    val releaseYear: String?,
    @ColumnInfo(name = "Genre")
    val primaryGenreName: String?,
    @ColumnInfo(name = "Country")
    val country: String?,
    @ColumnInfo(name = "Duration")
    val duration: String?,
    @ColumnInfo(name = "Preview_Url")
    val previewUrl: String?,
    @ColumnInfo(name = "Favourite")
    val isFavorite: Boolean?
)