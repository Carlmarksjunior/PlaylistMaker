package com.practicum.playlistmaker.presentation.media.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.db.favouritesTracks.FavouritesTracksInteractor
import com.practicum.playlistmaker.presentation.media.view_model.state.FavoriteTracksState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favoriteTracksInteractor: FavouritesTracksInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavoriteTracksState>()
    fun observeStateLiveData(): LiveData<FavoriteTracksState> = stateLiveData


    fun startDb() {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteTracksInteractor.getAllTracks().collect {
                if (it.isEmpty()) {
                    renderState(FavoriteTracksState.Empty(message = R.string.mediaIsempty))
                } else {
                    renderState(FavoriteTracksState.Content(it))
                    Log.e("meesage", "asd $it")
                }
            }
        }
    }

    private fun renderState(state: FavoriteTracksState) {
        stateLiveData.postValue(state)

    }

}