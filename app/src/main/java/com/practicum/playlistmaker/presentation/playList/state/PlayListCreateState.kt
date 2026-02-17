package com.practicum.playlistmaker.presentation.playList.state

data class PlayListCreateState (
    val albumName: String?="",
    val albumImagePath: String?="",
    val albumDescription: String?=null)