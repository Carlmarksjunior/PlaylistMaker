package com.practicum.playlistmaker.presentation.detailsPlayList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.db.favouritesTracks.FavouritesTracksInteractor
import com.practicum.playlistmaker.domain.db.playLists.AlbumInteractor
import com.practicum.playlistmaker.presentation.detailsPlayList.state.DetailsPlayListState
import com.practicum.playlistmaker.presentation.search.view_model.state.TrackState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsPlayListViewModel(
    private val albumsInteractor: AlbumInteractor,
    private val favouritesTracksInteractor: FavouritesTracksInteractor,
) : ViewModel() {


    private val albumState = MutableLiveData<DetailsPlayListState>()
    fun observeAlbumState(): LiveData<DetailsPlayListState> = albumState


    private val tracksState = MutableLiveData<TrackState>()
    fun observeTracksState(): LiveData<TrackState> = tracksState


    private val deleteTrackState = MutableLiveData<Boolean>(false)
    fun observeDeleteTracksState(): LiveData<Boolean> = deleteTrackState

    private val shareAlbumState = MutableLiveData<DetailsPlayListState>()
    fun observeShareAlbumState(): LiveData<DetailsPlayListState> = shareAlbumState


    private var listTracksIdsInAlbum: MutableList<String> = mutableListOf()

    private var tracksCountInAlbum: Int = 0

    private var listAllTrackInAllAlbum: MutableList<String> = mutableListOf()


    fun getAlbum(albumId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            albumsInteractor.getAlbumFromId(albumId).collect {
                albumState.postValue(DetailsPlayListState.Content(it))
                getTracks(it.tracksIds)
            }
        }
    }

    fun getTracks(tracksIds: List<String>) {
        viewModelScope.launch {
            albumsInteractor.getTracksByIds(tracksIds).collect {
                if (it.isNotEmpty()) {
                    it.map {
                        it.isFavorite =
                            favouritesTracksInteractor.isTrackExists(it.trackId.toString())
                    }
                    tracksState.postValue(TrackState.Content(tracks = it))
                } else {
                    tracksState.postValue(TrackState.Empty(""))
                }

            }
        }
    }

    fun deleteTrack(trackId: Int, albumId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            albumsInteractor.getAlbumFromId(albumId).collect {
                listTracksIdsInAlbum.clear()
                listTracksIdsInAlbum = it.tracksIds.toMutableList()
                listTracksIdsInAlbum.remove(trackId.toString())
                tracksCountInAlbum = it.tracksCount - 1
            }
            albumsInteractor.updateTracksInfoInAlbum(
                albumId,
                Gson().toJson(listTracksIdsInAlbum),
                tracksCountInAlbum
            )
            deleteTrackState.postValue(true)
            albumsInteractor.getAllAlbums().collect {
                listAllTrackInAllAlbum.clear()
                it.forEach { (it.tracksIds.forEach { listAllTrackInAllAlbum.add(it) }) }
                if (!listAllTrackInAllAlbum.contains(trackId.toString())) {
                    albumsInteractor.deleteTrackById(trackId)
                } else {}
            }
        }

    }

    fun deleteAlbum(albumId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            albumsInteractor.getAlbumFromId(albumId).collect {
                listTracksIdsInAlbum.clear()
                listTracksIdsInAlbum = it.tracksIds.toMutableList()
            }
            albumsInteractor.deleteAlbumsById(albumId)
            albumsInteractor.getAllAlbums().collect {

                listAllTrackInAllAlbum.clear()
                it.forEach { (it.tracksIds.forEach { listAllTrackInAllAlbum.add(it) }) }
                listTracksIdsInAlbum.forEach {
                    if (!listAllTrackInAllAlbum.contains(it)){
                    albumsInteractor.deleteTrackById(it.toInt())
                    }else{} }
            }
        }
    }

    fun shareTracksInPlayList(albumId: Int) {
        viewModelScope.launch {
            albumsInteractor.getAlbumFromId(albumId).collect {
                if (it.tracksCount!=0){
                    shareAlbumState.postValue(DetailsPlayListState.Content(it))
                }else{
                    shareAlbumState.postValue(DetailsPlayListState.Empty(R.string.track_in_album_Empty))
                }
            }
        }
    }


}