package com.practicum.playlistmaker.domain.api.interactor

import com.practicum.playlistmaker.domain.impl.PlayerState

interface AudioPlayerInteractor {
    fun setDataSource(url: String)

    fun prepareAsync()

    fun setOnPreparedListener(listener: () -> Unit)

    fun setOnCompletionListener(listener: () -> Unit)
    fun play()

    fun pause()

    fun release()

    fun getCurrentPosition():Int

    fun getCurrentState(): PlayerState

    fun togglePlayPause()
}