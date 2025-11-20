package com.practicum.playlistmaker.data.preferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.search.model.Track

class SharedPreferenceManager(private val gson: Gson,
                              private val sharedPreferencesHistory: SharedPreferences,
                              private val sharedPreferencesSettings: SharedPreferences ) {


    companion object {
         const val HISTORY_KEY = "search_history"
         const val SWITCH_MATERIAL_KEY = "key_for_switch_material"
    }

    fun getSaveTracks(): List<Track> {
        val historyJson = sharedPreferencesHistory.getString(HISTORY_KEY, null)
        return if (historyJson != null) {
            val type = object : TypeToken<List<Track>>() {}.type
            gson.fromJson(historyJson, type) ?: emptyList()
        } else {
            emptyList()
        }
    }

    fun saveTrack(track: Track) {
        val listHistory = getSaveTracks().toMutableList()
        val index = listHistory.indexOfFirst { it.trackId == track.trackId }
        if (index != -1) {
            listHistory.removeAt(index)
        }
        listHistory.add(0, track)
        if (listHistory.size > 10) {
            listHistory.removeAt(listHistory.size - 1)
        }
        val historyJson = gson.toJson(listHistory)
        sharedPreferencesHistory.edit()
            .putString(HISTORY_KEY, historyJson)
            .apply()
    }

    fun clearHistory() {
        sharedPreferencesHistory.edit()
            .remove(HISTORY_KEY)
            .apply()

    }

    fun saveTheme(darkTheme: Boolean){
        sharedPreferencesSettings.edit()
            .putBoolean(SWITCH_MATERIAL_KEY,darkTheme)
            .apply()
    }
    fun getTheme(): Boolean{
        return sharedPreferencesSettings.getBoolean(SWITCH_MATERIAL_KEY,false)
    }
}