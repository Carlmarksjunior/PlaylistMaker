package com.practicum.playlistmaker.domain.api.interactor

import com.practicum.playlistmaker.domain.models.SharingData
import com.practicum.playlistmaker.domain.models.SupportData

interface SupportSharingInteractor {
    fun getSharingAppData(): SharingData
    fun getSupportEmailData(): SupportData
    fun getUserAgreementUrl(): String
}