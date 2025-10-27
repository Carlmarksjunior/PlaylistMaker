package com.practicum.playlistmaker.domain.api.repository

interface ThemeRepository {
    fun saveTheme(darkTheme: Boolean)
    fun getTheme(): Boolean
}