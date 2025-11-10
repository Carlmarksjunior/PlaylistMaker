package com.practicum.playlistmaker.domain.settings

interface SettingsInteractor {
    fun saveTheme(darkTheme: Boolean)
    fun getTheme(): Boolean
}