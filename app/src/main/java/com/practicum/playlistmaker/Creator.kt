package com.practicum.playlistmaker

import android.content.Context
import android.media.MediaPlayer
import com.practicum.playlistmaker.data.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.data.HistoryRepositoryImpl
import com.practicum.playlistmaker.data.SupportSharingRepositoryImpl
import com.practicum.playlistmaker.data.ThemeRepositoryImpl
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.preference.SharedPreferenceManager
import com.practicum.playlistmaker.domain.api.interactor.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.api.interactor.HistoryInteractor
import com.practicum.playlistmaker.domain.api.interactor.SupportSharingInteractor
import com.practicum.playlistmaker.domain.api.interactor.ThemeInteractor
import com.practicum.playlistmaker.domain.api.interactor.TracksInteractor
import com.practicum.playlistmaker.domain.api.repository.AudioPlayerRepository
import com.practicum.playlistmaker.domain.api.repository.HistoryRepository
import com.practicum.playlistmaker.domain.api.repository.ThemeRepository
import com.practicum.playlistmaker.domain.api.repository.TracksRepository
import com.practicum.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.domain.impl.HistoryInteractorImpl
import com.practicum.playlistmaker.domain.impl.SupportSharingInteractorImpl
import com.practicum.playlistmaker.domain.impl.ThemeInteractorImpl
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository{
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
    private fun getHistoryRepository(context: Context): HistoryRepository {
        return HistoryRepositoryImpl(SharedPreferenceManager(context))
    }
    fun provideHistoryInteractor(context: Context): HistoryInteractor{
        return HistoryInteractorImpl(getHistoryRepository(context))
    }

    private fun getAudioPlayerRepository(): AudioPlayerRepository{
        val mediaPlayer = MediaPlayer()
        return AudioPlayerRepositoryImpl(mediaPlayer)
    }
    fun provideAudioPlayerInteractor(): AudioPlayerInteractor{
        return AudioPlayerInteractorImpl(getAudioPlayerRepository())
    }
    private fun getThemeRepository(context: Context): ThemeRepository{
        return ThemeRepositoryImpl(SharedPreferenceManager(context))
    }
    fun provideThemeInteractor(context: Context): ThemeInteractor{
        return ThemeInteractorImpl(getThemeRepository(context))
    }
    fun provideSupportSharingInteractor(context: Context): SupportSharingInteractor {
        val repository = SupportSharingRepositoryImpl(context)
        return SupportSharingInteractorImpl(repository)
    }
}