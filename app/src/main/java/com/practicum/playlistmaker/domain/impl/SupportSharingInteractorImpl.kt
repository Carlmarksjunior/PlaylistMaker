package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.interactor.SupportSharingInteractor
import com.practicum.playlistmaker.domain.api.repository.SupportSharingRepository
import com.practicum.playlistmaker.domain.models.SharingData
import com.practicum.playlistmaker.domain.models.SupportData

class SupportSharingInteractorImpl(private val repository: SupportSharingRepository):
    SupportSharingInteractor {
    override fun getSharingAppData(): SharingData = repository.getSharingAppData()
    override fun getSupportEmailData(): SupportData = repository.getSupportEmailData()
    override fun getUserAgreementUrl(): String = repository.getUserAgreementUrl()
}