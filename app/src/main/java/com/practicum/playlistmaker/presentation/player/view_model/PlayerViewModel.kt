package com.practicum.playlistmaker.presentation.player.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.db.favouritesTracks.FavouritesTracksInteractor
import com.practicum.playlistmaker.domain.db.playLists.AlbumInteractor
import com.practicum.playlistmaker.domain.db.playLists.model.Album
import com.practicum.playlistmaker.domain.player.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.presentation.media.view_model.state.PlayListState
import com.practicum.playlistmaker.presentation.player.view_model.state.PlayerState
import com.practicum.playlistmaker.presentation.player.view_model.state.TrackInAlbumState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val mediaPlayer: AudioPlayerInteractor,
    private val favouritesTracksInteractor: FavouritesTracksInteractor,
    private val albumInteractor: AlbumInteractor,
    private val context: Context
) : ViewModel() {
    companion object {

        private const val MUSIC_TIMER_DELAY = 300L
    }

    private val albumsLiveData = MutableLiveData<PlayListState>()
    fun observeAlbumsLiveData(): LiveData<PlayListState> = albumsLiveData


    private val playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData

    private val trackInAlbumLiveData = MutableLiveData<TrackInAlbumState>()
    fun observeTrackInAlbumLiveData(): LiveData<TrackInAlbumState> = trackInAlbumLiveData

    private val isFavoriteLiveData = MutableLiveData<Boolean>()
    fun observeIsFavoriteLiveData(): LiveData<Boolean> = isFavoriteLiveData


    private var timerJob: Job? = null
    private var isPlayerState = true

    fun insertTrackInAlbum(album: Album, track: Track) {
        viewModelScope.launch {

            val index = album.tracksIds.indexOfFirst { it.toInt() == track.trackId }
            if (index == -1) {
                val trackCount = album.tracksCount + 1
                val tracksIds = album.tracksIds.toMutableList()
                tracksIds.add(track.trackId.toString())
                albumInteractor.insertTrackInAlbum(track)
                albumInteractor.updateTracksInfoInAlbum(
                    album.id,
                    Gson().toJson(tracksIds),
                    trackCount
                )
                trackInAlbumLiveData.postValue(TrackInAlbumState.isSucces(album.albumName))
            } else {
                trackInAlbumLiveData.postValue(TrackInAlbumState.isFail(album.albumName))
            }

        }
    }

    fun getAllAlbums() {
        viewModelScope.launch {
            albumInteractor.getAllAlbums().collect {
                if (it.isNotEmpty()) {
                    albumsLiveData.postValue(PlayListState.Content(it))
                    Log.d("123", "$it")
                } else {
                    albumsLiveData.postValue(PlayListState.Empty(message = context.getString(R.string.playListIsEmpty)))
                }

            }
        }
    }

    private fun startTimerUpdate() {
        timerJob = viewModelScope.launch {
            playerStateLiveData.postValue(PlayerState.Playing(getCurrentProgress()))
            delay(MUSIC_TIMER_DELAY)
            if (playerStateLiveData.value is PlayerState.Playing) {
                startTimerUpdate()
            }
        }
    }

    private fun getCurrentProgress(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(mediaPlayer.getCurrentPosition())
    }

    fun onFavoriteClicked(track: Track) {

        if (track.isFavorite == false) {
            track.isFavorite = true
            viewModelScope.launch(Dispatchers.IO) {
                favouritesTracksInteractor.insertTrack(track)
            }
            isFavoriteLiveData.postValue(true)

        } else {
            viewModelScope.launch(Dispatchers.IO) {
                favouritesTracksInteractor.deleteTrack(track)
            }
            isFavoriteLiveData.postValue(false)

        }

    }


    fun preparePlayer(url: String) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStateLiveData.postValue(PlayerState.Prepared())
        }
        mediaPlayer.setOnCompletionListener {
            isPlayerState = true
            playerStateLiveData.postValue(PlayerState.Prepared())
        }
    }

    fun onPlayButtonClicked() {
        when (playerStateLiveData.value) {
            is PlayerState.Playing -> pausePlayer()
            is PlayerState.Prepared, is PlayerState.Paused -> startPlayer()
            else -> {}
        }
    }

    private fun startPlayer() {
        mediaPlayer.play()
        playerStateLiveData.postValue(PlayerState.Playing(getCurrentProgress()))
        startTimerUpdate()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        playerStateLiveData.postValue(PlayerState.Paused(getCurrentProgress()))
    }


    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()

    }

}



