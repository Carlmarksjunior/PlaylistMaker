package com.practicum.playlistmaker.data

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.repository.SupportSharingRepository
import com.practicum.playlistmaker.domain.models.SharingData
import com.practicum.playlistmaker.domain.models.SupportData

class SupportSharingRepositoryImpl(
    private val context: Context
) : SupportSharingRepository {

    override fun getSharingAppData(): SharingData {
        return SharingData(context.getString(R.string.url_YP))
    }

    override fun getSupportEmailData(): SupportData {
        return SupportData(
            email = context.getString(R.string.my_email),
            subject = context.getString(R.string.subject_to_mail),
            message = context.getString(R.string.message_to_mail)
        )
    }

    override fun getUserAgreementUrl(): String {
        return context.getString(R.string.practicum_offer)
    }
}