package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.creator.Resource
import com.practicum.playlistmaker.domain.search.model.Track

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}