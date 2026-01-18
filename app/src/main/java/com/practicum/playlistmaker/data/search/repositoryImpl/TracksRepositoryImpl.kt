package com.practicum.playlistmaker.data.search.repositoryImpl

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.search.NetworkClient
import com.practicum.playlistmaker.data.search.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.search.dto.TracksSearchResponse
import com.practicum.playlistmaker.domain.search.TracksRepository
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient,private val context: Context) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(context.getString(R.string.disconnect_message)))
            }
            200 -> {
                emit(Resource.Success((response as TracksSearchResponse).results.map {
                    Track(
                        it.trackName,
                        it.artistName,
                        SimpleDateFormat("mm:ss", Locale.getDefault())
                            .format(Date(it.trackTimeMillis!!)),
                        it.artworkUrl100,
                        it.previewUrl,
                        it.trackId,
                        it.country,
                        it.primaryGenreName,
                        it.collectionName,
                        it.releaseDate?.substring(0, 4)?: ""
                    )
                }))
            }
            else -> {
                emit(Resource.Error(context.getString(R.string.server_error)))
            }
        }
    }
}