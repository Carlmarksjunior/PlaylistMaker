package com.practicum.playlistmaker.domain.sharing.impl

import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.SharingRepository
import com.practicum.playlistmaker.domain.sharing.model.SharingData
import com.practicum.playlistmaker.domain.sharing.model.SupportData

class SharingInteractorImpl(private val repository: SharingRepository):
    SharingInteractor {
    override fun getSharingAppData(): SharingData = repository.getSharingAppData()
    override fun getSupportEmailData(): SupportData = repository.getSupportEmailData()
    override fun getUserAgreementUrl(): String = repository.getUserAgreementUrl()
}