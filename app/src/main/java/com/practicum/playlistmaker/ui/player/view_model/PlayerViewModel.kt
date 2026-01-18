package com.practicum.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.player.AudioPlayerInteractor
import com.practicum.playlistmaker.ui.player.state.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val mediaPlayer: AudioPlayerInteractor) : ViewModel() {
    companion object {

        private const val MUSIC_TIMER_DELAY = 300L
    }

    private val playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData


    private var timerJob: Job? = null
    private var isPlayerState = true



    private fun startTimerUpdate() {
        timerJob = viewModelScope.launch {
            playerStateLiveData.postValue(PlayerState.Playing(getCurrentProgress()))
            delay(MUSIC_TIMER_DELAY)
            if (playerStateLiveData.value is PlayerState.Playing) {
                startTimerUpdate()
            }
        }
    }

    private fun getCurrentProgress(): String{
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.getCurrentPosition())
    }



    fun preparePlayer(url: String) {
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
        when(playerStateLiveData.value) {
            is PlayerState.Playing -> pausePlayer()
            is PlayerState.Prepared, is PlayerState.Paused ->startPlayer()
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



