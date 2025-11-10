package com.practicum.playlistmaker.ui.search.view_model

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.search.HistoryInteractor
import com.practicum.playlistmaker.domain.search.TracksInteractor
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.search.state.TrackState

class SearchViewModel(private val disconnectMessage: String,
                      private val emptyResultMessage: String,
                      private val tracksInteractor: TracksInteractor,
                      private val historyInteractor: HistoryInteractor): ViewModel() {
    companion object{
        private  val SEARCH_REQUEST_TOKEN = Any()
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        fun getFactory(context: Context ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]as Application)
                SearchViewModel(disconnectMessage = context.getString(R.string.disconnect_message),
                    emptyResultMessage = context.getString(R.string.result_is_empty),
                    Creator.provideTracksInteractor(context),
                    Creator.provideHistoryInteractor(context))
            }
        }
    }




    private val stateLiveData  = MutableLiveData<TrackState>()
    fun observeStateLiveData(): LiveData<TrackState> = stateLiveData

    private val historyLiveData = MutableLiveData<List<Track>>()
    fun observeHistoryLiveData() = historyLiveData

    private val historySearch = mutableListOf<Track>()

    init {
        historyLiveData.postValue(historyInteractor.getSaveTracks().toMutableList())
        historySearch.addAll(historyInteractor.getSaveTracks().toMutableList())
    }

    private var latestSearchText: String? = null

    private val handler = Handler(Looper.getMainLooper())

    fun searchDebounce(changedText: String) {

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    private fun searchRequest(trackName: String) {
        if (trackName.isNotEmpty()) {
            renderState(TrackState.Loading)
            tracksInteractor.searchTracks(trackName, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                    handler.post {
                        val tracks = mutableListOf<Track>()

                        if (foundTracks!=null){
                            tracks.addAll(foundTracks)
                        }
                        when{
                            errorMessage!=null->{
                                renderState(
                                    TrackState.Error(
                                        errorMessage =disconnectMessage,
                                    )
                                )
                            }
                            tracks.isEmpty()->{
                                renderState(
                                    TrackState.Empty(
                                        message = emptyResultMessage
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
                }
            })
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
        historyLiveData.postValue(historySearch)

    }

    fun clearHistoryTracks(){
        historyInteractor.clearHistory()
        historySearch.clear()
        historyLiveData.postValue(emptyList())
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages ( SEARCH_REQUEST_TOKEN )
    }

}