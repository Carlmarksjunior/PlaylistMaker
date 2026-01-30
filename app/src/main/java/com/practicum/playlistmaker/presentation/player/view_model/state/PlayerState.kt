package com.practicum.playlistmaker.ui.player.state

sealed class PlayerState( val progress: String, var isFavorite: Boolean?) {

    class Default : PlayerState( "00:00", null)

    class Prepared : PlayerState( "00:00",null)

    class Playing(progress: String) : PlayerState( progress,null)

    class Paused(progress: String) : PlayerState( progress,null)
}