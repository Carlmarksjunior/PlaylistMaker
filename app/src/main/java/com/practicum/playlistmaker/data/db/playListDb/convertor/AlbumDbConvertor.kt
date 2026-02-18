package com.practicum.playlistmaker.data.db.playListDb.convertor

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.db.playListDb.entity.AlbumEntity
import com.practicum.playlistmaker.domain.db.playLists.model.Album

class AlbumDbConvertor {
    fun map(album: Album): AlbumEntity {
        return AlbumEntity(
            id = album.id,
            albumName = album.albumName,
            albumDescription=album.albumDescription,
            pathImage=album.pathImage,
            tracksIds= Gson().toJson(album.tracksIds),
            tracksCount = album.tracksCount
        )
    }
    val type = object : TypeToken<List<String>>() {}.type
    fun map(albumEntity: AlbumEntity): Album{
        return Album(
            id = albumEntity.id,
            albumName = albumEntity.albumName,
            albumDescription=albumEntity.albumDescription,
            pathImage=albumEntity.pathImage,
            tracksIds=  Gson().fromJson(albumEntity.tracksIds,type),
            tracksCount = albumEntity.tracksCount
        )
    }

}