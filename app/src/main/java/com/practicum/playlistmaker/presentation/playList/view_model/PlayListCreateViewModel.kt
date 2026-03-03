package com.practicum.playlistmaker.presentation.playList.view_model

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.db.playLists.AlbumInteractor
import com.practicum.playlistmaker.domain.db.playLists.model.Album
import com.practicum.playlistmaker.presentation.playList.state.PlayListCreateState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

open class PlayListCreateViewModel(private val albumInteractor: AlbumInteractor,
    private val context: Context): ViewModel() {

    open val albumLiveData = MutableLiveData<PlayListCreateState>(PlayListCreateState())
    open fun observerAlbumLiveData(): LiveData<PlayListCreateState> = albumLiveData

    private val insertAlbumState = MutableLiveData<Boolean>()
    fun observeInsertAlbumState(): LiveData<Boolean> = insertAlbumState
    fun saveNameAlbum(name: String){
        albumLiveData.postValue(albumLiveData.value?.copy(albumName = name))

    }

    fun saveDescriptionAlbum(description: String){
        albumLiveData.postValue(albumLiveData.value?.copy(albumDescription = description))
    }
    fun insertAlbum(album: Album){
        viewModelScope.launch(Dispatchers.IO) {
            if (!albumInteractor.isAlbumExists(album.albumName)){
                albumInteractor.insertAlbum(album)
                insertAlbumState.postValue(true)
            }else{
                insertAlbumState.postValue(false)
            }

        }
    }
    @SuppressLint("SuspiciousIndentation")
    open fun saveImageToPrivateStorage(uri: Uri, name: String) {
        viewModelScope.launch(Dispatchers.IO) {

        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")

        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val file = File(filePath, name)
            albumLiveData.postValue(albumLiveData.value?.copy(albumImagePath = file.toString()))
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        }

    }

}