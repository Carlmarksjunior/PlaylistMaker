package com.practicum.playlistmaker.domain.settings

interface SettingsRepository {
    fun saveTheme(darkTheme: Boolean)
    fun getTheme(): Boolean
}