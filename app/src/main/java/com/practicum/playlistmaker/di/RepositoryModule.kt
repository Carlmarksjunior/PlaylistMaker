package com.practicum.playlistmaker.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.db.favouritesTracksDb.FavouritesTracksRepositoryImpl
import com.practicum.playlistmaker.data.db.favouritesTracksDb.convertor.TrackDbConvertor
import com.practicum.playlistmaker.data.db.playListDb.AlbumsRepositoryImpl
import com.practicum.playlistmaker.data.db.playListDb.convertor.AlbumDbConvertor
import com.practicum.playlistmaker.data.db.tracksInAlbumDb.convertor.TrackInAlbumDbConvertor
import com.practicum.playlistmaker.data.player.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.data.search.HistoryRepositoryImpl
import com.practicum.playlistmaker.data.search.repositoryImpl.TracksRepositoryImpl
import com.practicum.playlistmaker.data.settings.ThemeRepositoryImpl
import com.practicum.playlistmaker.data.sharing.ExternalNavigatorImpl
import com.practicum.playlistmaker.domain.db.favouritesTracks.FavouritesTracksRepository
import com.practicum.playlistmaker.domain.db.playLists.AlbumsRepository
import com.practicum.playlistmaker.domain.player.AudioPlayerRepository
import com.practicum.playlistmaker.domain.search.HistoryRepository
import com.practicum.playlistmaker.domain.search.TracksRepository
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.domain.sharing.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module{

    single<AlbumsRepository> {
        AlbumsRepositoryImpl(get(),get(),get(),get())
    }

    single<FavouritesTracksRepository> {
        FavouritesTracksRepositoryImpl(get(),get())
    }

    factory { TrackInAlbumDbConvertor() }
    factory { AlbumDbConvertor() }

    factory { TrackDbConvertor() }

    factory<MediaPlayer> {
        MediaPlayer()
    }

    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(MediaPlayer())
    }

    factory<HistoryRepository> {
        HistoryRepositoryImpl(get(),get())
    }

    factory<TracksRepository> {
        TracksRepositoryImpl(get(),androidContext(),get())
    }
    factory<SettingsRepository> {
        ThemeRepositoryImpl(get())
    }
    factory<SharingRepository> {
        ExternalNavigatorImpl(get())
    }
}