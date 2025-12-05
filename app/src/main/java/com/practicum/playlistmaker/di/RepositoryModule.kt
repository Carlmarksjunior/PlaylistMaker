package com.practicum.playlistmaker.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.player.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.data.search.HistoryRepositoryImpl
import com.practicum.playlistmaker.data.search.repositoryImpl.TracksRepositoryImpl
import com.practicum.playlistmaker.data.settings.ThemeRepositoryImpl
import com.practicum.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.practicum.playlistmaker.domain.player.AudioPlayerRepository
import com.practicum.playlistmaker.domain.search.HistoryRepository
import com.practicum.playlistmaker.domain.search.TracksRepository
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.domain.sharing.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module{

    factory<MediaPlayer> {
        MediaPlayer()
    }

    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(MediaPlayer())
    }

    factory<HistoryRepository> {
        HistoryRepositoryImpl(get())
    }

    factory<TracksRepository> {
        TracksRepositoryImpl(get(),androidContext())
    }
    factory<SettingsRepository> {
        ThemeRepositoryImpl(get())
    }
    factory<SharingRepository> {
        ExternalNavigatorImpl(get())
    }
}