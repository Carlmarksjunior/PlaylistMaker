package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.domain.search.TracksInteractor
import com.practicum.playlistmaker.domain.search.TracksRepository
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {


    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
            return repository.searchTracks(expression).map {
                when(it){
                    is Resource.Success -> Pair(it.data,null)
                    is Resource.Error -> Pair(null,it.message)
                }
            }

    }
}