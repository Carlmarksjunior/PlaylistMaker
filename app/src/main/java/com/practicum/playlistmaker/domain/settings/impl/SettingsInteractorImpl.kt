package com.practicum.playlistmaker.domain.settings.impl

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.SettingsRepository

class SettingsInteractorImpl(private val themeRepository: SettingsRepository): SettingsInteractor {
    override fun saveTheme(darkTheme: Boolean) {
        themeRepository.saveTheme(darkTheme)
        AppCompatDelegate.setDefaultNightMode(
            if(darkTheme){
                AppCompatDelegate.MODE_NIGHT_YES
            }else{
                AppCompatDelegate.MODE_NIGHT_NO

            }
        )

    }

    override fun getTheme(): Boolean {
        val darkTheme =themeRepository.getTheme()
        AppCompatDelegate.setDefaultNightMode(
            if(darkTheme){
                AppCompatDelegate.MODE_NIGHT_YES
            }else {
                AppCompatDelegate.MODE_NIGHT_NO
            })
        return darkTheme
    }
}