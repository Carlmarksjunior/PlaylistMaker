package com.practicum.playlistmaker.data.sharing

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.sharing.SharingRepository
import com.practicum.playlistmaker.domain.sharing.model.SharingData
import com.practicum.playlistmaker.domain.sharing.model.SupportData

class ExternalNavigatorImpl(
    private val context: Context
) : SharingRepository {

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