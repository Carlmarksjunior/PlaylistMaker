package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SearchHistory(private val sharedPreferences: SharedPreferences) {
    private val gson = Gson()


    companion object {
        private const val HISTORY_KEY = "search_history"
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
}
