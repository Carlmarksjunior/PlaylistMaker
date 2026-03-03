package com.practicum.playlistmaker.presentation.detailsPlayList.state

import androidx.annotation.StringRes
import com.practicum.playlistmaker.domain.db.playLists.model.Album

sealed class DetailsPlayListState {

    class Content(val album: Album): DetailsPlayListState()

    class Empty(@StringRes val message: Int): DetailsPlayListState()
}