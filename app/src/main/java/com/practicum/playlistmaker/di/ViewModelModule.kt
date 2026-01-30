package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.presentation.media.view_model.FavoriteTracksViewModel
import com.practicum.playlistmaker.presentation.media.view_model.PlayListViewModel
import com.practicum.playlistmaker.presentation.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.presentation.search.view_model.SearchViewModel
import com.practicum.playlistmaker.presentation.settings.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel{
        PlayerViewModel(get(),get())
    }

    viewModel{
        SearchViewModel(androidContext(), get(),get())
    }

    viewModel{
        SettingsViewModel(get(),get())
    }

    viewModel {
        FavoriteTracksViewModel(androidContext(),get())
    }

    viewModel {
        PlayListViewModel()
    }



}