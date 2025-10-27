package com.practicum.playlistmaker.domain.impl

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.domain.api.interactor.ThemeInteractor
import com.practicum.playlistmaker.domain.api.repository.ThemeRepository

class ThemeInteractorImpl(private val themeRepository: ThemeRepository): ThemeInteractor {
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