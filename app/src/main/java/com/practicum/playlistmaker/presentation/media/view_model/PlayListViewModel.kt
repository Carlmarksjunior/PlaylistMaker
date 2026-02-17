package com.practicum.playlistmaker.presentation.media.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.db.playLists.AlbumInteractor
import com.practicum.playlistmaker.presentation.media.view_model.state.PlayListState
import kotlinx.coroutines.launch

class PlayListViewModel(
    private val albumInteractor: AlbumInteractor,
    private val context: Context
) : ViewModel() {

    private val playerListStateLiveData = MutableLiveData<PlayListState>()

    fun observePlayerListLiveData(): LiveData<PlayListState> = playerListStateLiveData

    fun getAllAlbums() {
        viewModelScope.launch {
            albumInteractor.getAllAlbums().collect {
                if (it.isNotEmpty()) {
                    playerListStateLiveData.postValue(PlayListState.Content(it))
                } else {
                    playerListStateLiveData.postValue(
                        PlayListState.Empty(
                            message = R.string.playListIsEmpty
                        )
                    )
                }

            }
        }
    }
}