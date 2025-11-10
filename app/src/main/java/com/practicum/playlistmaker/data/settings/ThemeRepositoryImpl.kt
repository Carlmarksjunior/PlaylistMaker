package com.practicum.playlistmaker.data.settings

import com.practicum.playlistmaker.data.preferences.SharedPreferenceManager
import com.practicum.playlistmaker.domain.settings.SettingsRepository

class ThemeRepositoryImpl(private val sharedPreferenceManager: SharedPreferenceManager):
    SettingsRepository {
    override fun saveTheme(darkTheme: Boolean) {
        sharedPreferenceManager.saveTheme(darkTheme)
    }

    override fun getTheme(): Boolean {
        return sharedPreferenceManager.getTheme()
    }
}