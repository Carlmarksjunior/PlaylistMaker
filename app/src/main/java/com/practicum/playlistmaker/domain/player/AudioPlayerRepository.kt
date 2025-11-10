package com.practicum.playlistmaker.domain.player

interface AudioPlayerRepository {
    fun setDataSource(url: String)
    fun prepareAsync()
    fun start()
    fun pause()
    fun release()
    fun getCurrentPosition(): Int
    fun setOnPreparedListener(listener: () -> Unit)
    fun setOnCompletionListener(listener: () -> Unit)
}