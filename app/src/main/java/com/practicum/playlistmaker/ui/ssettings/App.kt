package com.practicum.playlistmaker.ui.ssettings

import android.app.Application
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.domain.api.interactor.ThemeInteractor


class App : Application() {
    var darkTheme = false
    private lateinit var themeInteractor: ThemeInteractor

    override fun onCreate() {
        themeInteractor = Creator.provideThemeInteractor(this)
        super.onCreate()
        darkTheme = themeInteractor.getTheme()
    }

    fun switchTheme(darkThemeEnabled: Boolean){
        darkTheme = darkThemeEnabled
        themeInteractor.saveTheme(darkTheme)
    }
}