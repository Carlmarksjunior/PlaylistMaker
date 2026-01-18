package com.practicum.playlistmaker.ui.search.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.search.HistoryInteractor
import com.practicum.playlistmaker.domain.search.TracksInteractor
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.search.state.TrackState
import com.practicum.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(private val context: Context,
                      private val tracksInteractor: TracksInteractor,
                      private val historyInteractor: HistoryInteractor): ViewModel() {
    companion object{

        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }




    private val stateLiveData  = MutableLiveData<TrackState>()
    fun observeStateLiveData(): LiveData<TrackState> = stateLiveData



    private val historySearch = mutableListOf<Track>()

    init {
        stateLiveData.postValue(TrackState.History(historyInteractor.getSaveTracks().toMutableList()))
        historySearch.addAll(historyInteractor.getSaveTracks().toMutableList())
    }

    private var latestSearchText: String? = null
    
    private val clickDebounce:(String)-> Unit = debounce<String>(SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        false,
        {searchRequest(it)})

    fun searchDebounce(changedText: String) {
            this.latestSearchText = changedText
            clickDebounce(changedText)

    }


    private fun searchRequest(trackName: String) {
        if (trackName.isNotEmpty()) {
            renderState(TrackState.Loading)

            viewModelScope.launch {
                tracksInteractor.searchTracks(trackName)
                    .collect {processResult(it.first,it.second)}
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?,errorMessage: String?){
        val tracks = mutableListOf<Track>()

        if (foundTracks!=null){
            tracks.addAll(foundTracks)
        }
        when{
            errorMessage!=null->{
                renderState(
                    TrackState.Error(
                        errorMessage =context.getString(R.string.disconnect_message),
                    )
                )
            }
            tracks.isEmpty()->{
                renderState(
                    TrackState.Empty(
                        message = context.getString(R.string.result_is_empty)
                    )
                )
            }
            else->{
                renderState(
                    TrackState.Content(
                        tracks=tracks
                    )
                )
            }
        }
    }

    private fun renderState(state : TrackState){
        stateLiveData.postValue(state)

    }

    fun saveTrack(track: Track){
        val index =  historySearch.indexOfFirst { it.trackId == track.trackId }
        if (index!=-1){
            historySearch.removeAt(index)
        }
        historyInteractor.saveTrack(track)
        historySearch.add(track)
        stateLiveData.postValue(TrackState.History(historySearch))

    }

    fun clearHistoryTracks(){
        historyInteractor.clearHistory()
        historySearch.clear()
        stateLiveData.postValue(TrackState.History(emptyList()))
    }



}