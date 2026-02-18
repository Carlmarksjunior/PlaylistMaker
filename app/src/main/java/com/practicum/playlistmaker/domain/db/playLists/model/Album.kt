package com.practicum.playlistmaker.domain.db.playLists.model

data class Album(
    val id: Int=0,
    val albumName: String?,
    val albumDescription: String?,
    val pathImage: String?,
    val tracksIds: List<String> = mutableListOf(),
    val tracksCount: Int = 0)
