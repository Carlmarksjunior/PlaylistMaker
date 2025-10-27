package com.practicum.playlistmaker.data.preference

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.models.Track

class SharedPreferenceManager(private val context: Context) {
    private val gson = Gson()
    private val sharedPreferences = context.getSharedPreferences(HISTORY_KEY, Context.MODE_PRIVATE)
    val sharedPreferencesSettings = context.getSharedPreferences(PLAY_LIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)


    companion object {
        private const val HISTORY_KEY = "search_history"
        private const val PLAY_LIST_MAKER_PREFERENCES = "Settings"
        private const val SWITCH_MATERIAL_KEY = "key_for_switch_material"
    }

    fun getSaveTracks(): List<Track> {
        val historyJson = sharedPreferences.getString(HISTORY_KEY, null)
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
        sharedPreferences.edit()
            .putString(HISTORY_KEY, historyJson)
            .apply()
    }

    fun clearHistory() {
        sharedPreferences.edit()
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
