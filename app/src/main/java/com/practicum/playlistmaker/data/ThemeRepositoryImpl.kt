package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.preference.SharedPreferenceManager
import com.practicum.playlistmaker.domain.api.repository.ThemeRepository

class ThemeRepositoryImpl(private val sharedPreferenceManager: SharedPreferenceManager): ThemeRepository {
    override fun saveTheme(darkTheme: Boolean) {
        sharedPreferenceManager.saveTheme(darkTheme)
    }

    override fun getTheme(): Boolean {
        return sharedPreferenceManager.getTheme()
    }
}