package com.practicum.playlistmaker.creator

import android.content.Context
import android.media.MediaPlayer
import com.practicum.playlistmaker.data.player.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.data.preferences.SharedPreferenceManager
import com.practicum.playlistmaker.data.search.HistoryRepositoryImpl
import com.practicum.playlistmaker.data.search.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.search.repositoryImpl.TracksRepositoryImpl
import com.practicum.playlistmaker.data.settings.ThemeRepositoryImpl
import com.practicum.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.practicum.playlistmaker.domain.player.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.player.AudioPlayerRepository
import com.practicum.playlistmaker.domain.player.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.domain.search.HistoryInteractor
import com.practicum.playlistmaker.domain.search.HistoryRepository
import com.practicum.playlistmaker.domain.search.TracksInteractor
import com.practicum.playlistmaker.domain.search.TracksRepository
import com.practicum.playlistmaker.domain.search.impl.HistoryInteractorImpl
import com.practicum.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl

object Creator {
    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context),context)
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }
    private fun getHistoryRepository(context: Context): HistoryRepository {
        return HistoryRepositoryImpl(SharedPreferenceManager(context))
    }
    fun provideHistoryInteractor(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(getHistoryRepository(context))
    }

    private fun getAudioPlayerRepository(): AudioPlayerRepository {
        val mediaPlayer = MediaPlayer()
        return AudioPlayerRepositoryImpl(mediaPlayer)
    }
    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(getAudioPlayerRepository())
    }
    private fun getThemeRepository(context: Context): SettingsRepository {
        return ThemeRepositoryImpl(SharedPreferenceManager(context))
    }
    fun provideSettingInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getThemeRepository(context))
    }
    fun provideSharingInteractor(context: Context): SharingInteractor {
        val repository = ExternalNavigatorImpl(context)
        return SharingInteractorImpl(repository)
    }
}