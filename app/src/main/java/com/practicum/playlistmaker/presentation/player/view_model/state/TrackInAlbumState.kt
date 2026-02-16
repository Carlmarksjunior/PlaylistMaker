package com.practicum.playlistmaker.presentation.player.view_model.state

sealed class TrackInAlbumState {
    class isSucces(val name: String?): TrackInAlbumState()
    class isFail(val name: String?): TrackInAlbumState()
}