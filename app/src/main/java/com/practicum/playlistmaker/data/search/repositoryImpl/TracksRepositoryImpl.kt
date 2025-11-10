package com.practicum.playlistmaker.data.search.repositoryImpl

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Resource
import com.practicum.playlistmaker.data.search.NetworkClient
import com.practicum.playlistmaker.data.search.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.search.dto.TracksSearchResponse
import com.practicum.playlistmaker.domain.search.TracksRepository
import com.practicum.playlistmaker.domain.search.model.Track
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient,private val context: Context) : TracksRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        return when (response.resultCode) {
            -1 -> {
                Resource.Error(context.getString(R.string.disconnect_message))
            }
            200 -> {
                Resource.Success((response as TracksSearchResponse).results.map {
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
                        it.releaseDate.substring(0, 4)
                    )
                })
            }
            else -> {
                Resource.Error(context.getString(R.string.server_error))
            }
        }
    }
}