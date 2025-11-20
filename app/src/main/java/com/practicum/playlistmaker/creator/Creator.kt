package com.practicum.playlistmaker.creator

import android.media.MediaPlayer

object Creator {
//    private fun getTracksRepository(context: Context): TracksRepository {
//        return TracksRepositoryImpl(RetrofitNetworkClient(context),context)
//    }
//
//    fun provideTracksInteractor(context: Context): TracksInteractor {
//        return TracksInteractorImpl(getTracksRepository(context))
//    }
//    private fun getHistoryRepository(context: Context): HistoryRepository {
//        return HistoryRepositoryImpl(SharedPreferenceManager(context))
//    }
//    fun provideHistoryInteractor(context: Context): HistoryInteractor {
//        return HistoryInteractorImpl(getHistoryRepository(context))
//    }
     fun getMediaPlayer (): MediaPlayer{
        return MediaPlayer()
    }
//    private fun getAudioPlayerRepository(): AudioPlayerRepository {
//        val mediaPlayer = MediaPlayer()
//        return AudioPlayerRepositoryImpl(mediaPlayer)
//    }
//    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
//        return AudioPlayerInteractorImpl(getAudioPlayerRepository())
//    }
//    private fun getThemeRepository(context: Context): SettingsRepository {
//        return ThemeRepositoryImpl(SharedPreferenceManager(context))
//    }
//    fun provideSettingInteractor(context: Context): SettingsInteractor {
//        return SettingsInteractorImpl(getThemeRepository(context))
//    }
//    fun provideSharingInteractor(context: Context): SharingInteractor {
//        val repository = ExternalNavigatorImpl(context)
//        return SharingInteractorImpl(repository)
//    }
}