package com.practicum.playlistmaker.domain.player.impl

import com.practicum.playlistmaker.domain.player.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.player.AudioPlayerRepository


class AudioPlayerInteractorImpl(
    private val audioPlayerRepository: AudioPlayerRepository,
): AudioPlayerInteractor {
    private var currentState: PlayerState = PlayerState.DEFAULT

    override fun setDataSource(url: String) {
        audioPlayerRepository.setDataSource(url)
    }

    override fun prepareAsync() {
        audioPlayerRepository.prepareAsync()
        currentState = PlayerState.PREPARING
    }

    override fun setOnPreparedListener(listener: () -> Unit) {
        audioPlayerRepository.setOnPreparedListener(listener)
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        audioPlayerRepository.setOnCompletionListener(listener)
    }

    override fun play() {
        audioPlayerRepository.start()
        currentState = PlayerState.PLAYING
    }

    override fun pause() {
        audioPlayerRepository.pause()
        currentState = PlayerState.PAUSED
    }

    override fun release() {
        audioPlayerRepository.release()
        currentState = PlayerState.DEFAULT
    }

    override fun getCurrentPosition(): Int = audioPlayerRepository.getCurrentPosition()

    override fun getCurrentState(): PlayerState = currentState

    override fun togglePlayPause() {
        when (currentState) {
            PlayerState.PLAYING -> pause()
            PlayerState.PAUSED, PlayerState.PREPARED -> play()
            else -> {}
        }
    }
}

enum class PlayerState {
    DEFAULT, PREPARING, PREPARED, PLAYING, PAUSED
}