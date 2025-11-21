package com.practicum.playlistmaker.di

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.data.preferences.SharedPreferenceManager
import com.practicum.playlistmaker.data.search.NetworkClient
import com.practicum.playlistmaker.data.search.network.ItunesApiService
import com.practicum.playlistmaker.data.search.network.RetrofitNetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<ItunesApiService> {
        Retrofit.Builder()
            .baseUrl(RetrofitNetworkClient.ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApiService::class.java)
    }

    factory<Gson> {
        Gson()
    }

    single(named("sharedPreferencesHistory")) {
        androidContext().getSharedPreferences(
            SharedPreferenceManager.HISTORY_KEY,
            Context.MODE_PRIVATE
        )
    }

    single(named("sharedPreferencesSettings")) {
        androidContext().getSharedPreferences(
            SharedPreferenceManager.PLAY_LIST_MAKER_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    single<NetworkClient> {
        RetrofitNetworkClient(androidContext(), get())
    }

    single<SharedPreferenceManager> {
        SharedPreferenceManager(
            get(),
            get(named("sharedPreferencesHistory")),
            get(named("sharedPreferencesSettings"))
        )
    }


}


