package com.practicum.playlistmaker.domain.api.interactor

interface ThemeInteractor {
    fun saveTheme(darkTheme: Boolean)
    fun getTheme(): Boolean
}