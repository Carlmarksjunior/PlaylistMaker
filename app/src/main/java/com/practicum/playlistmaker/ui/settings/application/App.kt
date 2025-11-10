package com.practicum.playlistmaker.ui.settings.application

import android.app.Application
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.settings.SettingsInteractor

class App : Application() {
    var darkTheme = false
    private lateinit var themeInteractor: SettingsInteractor

    override fun onCreate() {
        themeInteractor = Creator.provideSettingInteractor(this)
        super.onCreate()
        darkTheme = themeInteractor.getTheme()
    }

}