package com.practicum.playlistmaker.domain.api.repository

import com.practicum.playlistmaker.domain.models.SharingData
import com.practicum.playlistmaker.domain.models.SupportData

interface SupportSharingRepository {
    fun getSharingAppData(): SharingData
    fun getSupportEmailData(): SupportData
    fun getUserAgreementUrl(): String
}