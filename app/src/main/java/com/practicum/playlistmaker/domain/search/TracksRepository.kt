package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}