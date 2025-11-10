package com.practicum.playlistmaker.domain.search.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(val trackName: String?,
                 val artistName: String? ,
                 val duration: String?,
                 val artworkUrl100: String?,
                 val previewUrl: String?,
                 val trackId: Int?,
                 val country: String?,
                 val primaryGenreName: String?,
                 val collectionName: String?,
                 val releaseYear: String?): Parcelable