package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.domain.api.repository.TracksRepository
import com.practicum.playlistmaker.domain.models.Track
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as TracksSearchResponse).results.mapNotNull {
                if (it.trackTimeMillis == null) {
                    null
                } else {
                    Track(
                        it.trackName,
                        it.artistName,
                        SimpleDateFormat("mm:ss", Locale.getDefault())
                            .format(Date(it.trackTimeMillis)),
                        it.artworkUrl100,
                        it.previewUrl,
                        it.trackId,
                        it.country,
                        it.primaryGenreName,
                        it.collectionName,
                        it.releaseDate?.substring(0, 4)
                    )
                }
            }
        }else if (response.resultCode == 400) {
            return emptyList()
        }else{
            throw IOException("No internet connection")
        }
    }
}