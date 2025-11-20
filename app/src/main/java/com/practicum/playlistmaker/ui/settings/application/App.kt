package com.practicum.playlistmaker.ui.settings.application

import android.app.Application
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModelModule
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    private val themeInteractor: SettingsInteractor by inject()

    var darkTheme = false

    override fun onCreate() {

        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule,interactorModule, repositoryModule, viewModelModule)
        }
        darkTheme = themeInteractor.getTheme()
    }

}