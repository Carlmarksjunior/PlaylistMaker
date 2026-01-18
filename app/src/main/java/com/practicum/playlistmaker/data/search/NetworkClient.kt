package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.data.search.dto.Response

interface NetworkClient {
    suspend fun doRequest (dto: Any): Response
}