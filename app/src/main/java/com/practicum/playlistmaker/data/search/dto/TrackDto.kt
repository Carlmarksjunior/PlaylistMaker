package com.practicum.playlistmaker.data.search.dto

data class TrackDto(val trackName: String,
                    val artistName: String ,
                    val trackTimeMillis: Long,
                    val artworkUrl100: String,
                    val previewUrl: String,
                    val trackId: Int,
                    val country: String,
                    val primaryGenreName: String,
                    val collectionName: String,
                    val releaseDate: String)