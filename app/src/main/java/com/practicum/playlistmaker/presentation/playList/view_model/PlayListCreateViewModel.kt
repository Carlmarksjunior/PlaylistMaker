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
import com.practicum.playlistmaker.presentation.settings.view_model.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class PlayListCreateViewModel(private val albumInteractor: AlbumInteractor,
    private val context: Context): ViewModel() {


    private val insertTrackLiveData = SingleLiveEvent<Boolean>()
    fun observerInsertTrackLiveData(): LiveData<Boolean> = insertTrackLiveData

    private val pathImageLiveData = MutableLiveData<String>()
    fun observePathImageLiveData(): LiveData<String> = pathImageLiveData
    fun insertAlbum(album: Album){
        viewModelScope.launch(Dispatchers.IO) {
            if (!albumInteractor.isAlbumExists(album.albumName)){
                albumInteractor.insertAlbum(album)
                insertTrackLiveData.postValue(true)

            }else{
                insertTrackLiveData.postValue(false)
            }

        }
    }
    @SuppressLint("SuspiciousIndentation")
    fun saveImageToPrivateStorage(uri: Uri, name: String) {
        viewModelScope.launch(Dispatchers.IO) {

        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")

        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val file = File(filePath, name)
            pathImageLiveData.postValue(file.toString())
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        }

    }

}