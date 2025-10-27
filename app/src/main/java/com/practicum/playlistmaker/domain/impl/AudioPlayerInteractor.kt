package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.interactor.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.api.repository.AudioPlayerRepository


class AudioPlayerInteractorImpl(
    private val mediaPlayerRepository: AudioPlayerRepository,
): AudioPlayerInteractor {
    private var currentState: PlayerState = PlayerState.DEFAULT

    override fun setDataSource(url: String) {
        mediaPlayerRepository.setDataSource(url)
    }

    override fun prepareAsync() {
        mediaPlayerRepository.prepareAsync()
        currentState = PlayerState.PREPARING
    }

    override fun setOnPreparedListener(listener: () -> Unit) {
        mediaPlayerRepository.setOnPreparedListener(listener)
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayerRepository.setOnCompletionListener(listener)
    }

    override fun play() {
        mediaPlayerRepository.start()
        currentState = PlayerState.PLAYING
    }

    override fun pause() {
        mediaPlayerRepository.pause()
        currentState = PlayerState.PAUSED
    }

    override fun release() {
        mediaPlayerRepository.release()
        currentState = PlayerState.DEFAULT
    }

    override fun getCurrentPosition(): Int = mediaPlayerRepository.getCurrentPosition()

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