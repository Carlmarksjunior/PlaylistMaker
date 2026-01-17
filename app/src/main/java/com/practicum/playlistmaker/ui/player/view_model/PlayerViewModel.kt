package com.practicum.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.player.AudioPlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val mediaPlayer: AudioPlayerInteractor) : ViewModel() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val MUSIC_TIMER_DELAY = 300L

    }

    private val playerStateLiveData = MutableLiveData(STATE_DEFAULT)
    fun observePlayerStateLiveData(): LiveData<Int> = playerStateLiveData

    private val timerLiveData = MutableLiveData("00:00")
    fun observeTimerLiveData(): LiveData<String> = timerLiveData

    private var timerJob: Job? = null
    private var isPlayerState = true



    private fun startTimerUpdate() {
        timerJob = viewModelScope.launch {
            timerLiveData.postValue(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.getCurrentPosition()))
            delay(MUSIC_TIMER_DELAY)
            if (playerStateLiveData.value == STATE_PLAYING) {
                startTimerUpdate()
            }
        }
    }



    fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerStateLiveData.postValue(STATE_PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            isPlayerState = true
            resetTimer()
            playerStateLiveData.postValue(STATE_PREPARED)
        }
    }
    fun onPlayButtonClicked() {
        when(playerStateLiveData.value) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.play()
        playerStateLiveData.postValue(STATE_PLAYING)
        startTimerUpdate()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        playerStateLiveData.postValue(STATE_PAUSED)
    }


    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()

    }

    private fun resetTimer() {
        timerLiveData.postValue("00:00")
    }
}



