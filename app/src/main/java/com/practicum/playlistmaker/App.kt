package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate


const val PLAY_LIST_MAKER_PREFERENCES = "play_List_maker_preferences"
const val SWITCH_MATERIAL_KEY = "key_for_switch_material"
class App : Application() {
    var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        val sharePrefs = getSharedPreferences(PLAY_LIST_MAKER_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharePrefs.getBoolean(SWITCH_MATERIAL_KEY,false)
        AppCompatDelegate.setDefaultNightMode(
            if(darkTheme){
                AppCompatDelegate.MODE_NIGHT_YES
            }else{
                AppCompatDelegate.MODE_NIGHT_NO

            }
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean){
        darkTheme = darkThemeEnabled
        val sharePrefs = getSharedPreferences(PLAY_LIST_MAKER_PREFERENCES, MODE_PRIVATE)
        sharePrefs.edit()
            .putBoolean(SWITCH_MATERIAL_KEY,darkTheme)
            .apply()
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled){
                AppCompatDelegate.MODE_NIGHT_YES
            }else{
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}