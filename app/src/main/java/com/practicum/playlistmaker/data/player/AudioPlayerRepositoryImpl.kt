package com.practicum.playlistmaker.data.player

import android.media.MediaPlayer
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.player.AudioPlayerRepository

class AudioPlayerRepositoryImpl() : AudioPlayerRepository {
    private var mediaPlayer: MediaPlayer? = null

    override fun setDataSource(url: String) {
        mediaPlayer = Creator.getMediaPlayer()
        mediaPlayer?.setDataSource(url)
    }

    override fun prepareAsync() {
        mediaPlayer?.prepareAsync()
    }

    override fun start() {
        mediaPlayer?.start()
    }

    override fun pause() {
        mediaPlayer?.pause()
    }

    override fun release() {
        mediaPlayer?.release()
    }

    override fun getCurrentPosition(): Int = mediaPlayer!!.currentPosition

    override fun setOnPreparedListener(listener: () -> Unit) {
        mediaPlayer?.setOnPreparedListener { listener() }
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayer?.setOnCompletionListener { listener() }
    }
}