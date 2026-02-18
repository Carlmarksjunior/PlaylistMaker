package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.db.favouritesTracks.FavouritesTracksInteractor
import com.practicum.playlistmaker.domain.db.favouritesTracks.impl.FavouritesTracksInteractorImpl
import com.practicum.playlistmaker.domain.db.playLists.AlbumInteractor
import com.practicum.playlistmaker.domain.db.playLists.impl.AlbumInteractorImpl
import com.practicum.playlistmaker.domain.player.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.domain.search.HistoryInteractor
import com.practicum.playlistmaker.domain.search.TracksInteractor
import com.practicum.playlistmaker.domain.search.impl.HistoryInteractorImpl
import com.practicum.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module{

    single<AlbumInteractor> {
        AlbumInteractorImpl(get())
    }

    single<FavouritesTracksInteractor> {
        FavouritesTracksInteractorImpl(get())
    }

    factory  <AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get())
    }
    factory<HistoryInteractor>{
        HistoryInteractorImpl(get())
    }
    factory<TracksInteractor>{
        TracksInteractorImpl(get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
    factory<SharingInteractor>{
        SharingInteractorImpl(get())
    }

}