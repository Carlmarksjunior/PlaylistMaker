package com.practicum.playlistmaker.domain.sharing

import com.practicum.playlistmaker.domain.sharing.model.SharingData
import com.practicum.playlistmaker.domain.sharing.model.SupportData

interface SharingInteractor {
    fun getSharingAppData(): SharingData
    fun getSupportEmailData(): SupportData
    fun getUserAgreementUrl(): String
}