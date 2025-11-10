package com.practicum.playlistmaker.ui.settings.view_model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.model.SharingData
import com.practicum.playlistmaker.domain.sharing.model.SupportData

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
): ViewModel() {

    private var sharingLiveData = MutableLiveData<SharingData>()
    fun observerSharing() = sharingLiveData

    private var supportLiveData = MutableLiveData<SupportData>()
    fun observerSupport() = supportLiveData

    private var userAgreementLiveData = MutableLiveData<String>()
    fun observerUserAgreement() = userAgreementLiveData

    private var themeLiveData = MutableLiveData<Boolean>(settingsInteractor.getTheme())
    fun observerSettings() = themeLiveData



    fun saveTheme(){
        val newTheme= !settingsInteractor.getTheme()
        themeLiveData.postValue(newTheme)
        settingsInteractor.saveTheme(newTheme)
    }

    fun sharing(){
        sharingLiveData.postValue(sharingInteractor.getSharingAppData())
    }
    fun supportEmailData(){
        supportLiveData.postValue(sharingInteractor.getSupportEmailData())
    }
    fun userAgreement(){
        userAgreementLiveData.postValue(sharingInteractor.getUserAgreementUrl())
    }



    companion object{
        fun getFactory(): ViewModelProvider.Factory =
            viewModelFactory{
                initializer {
                    val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]as Application)
                    SettingsViewModel(Creator.provideSharingInteractor(app),
                        Creator.provideSettingInteractor(app))
                }
            }
    }
}