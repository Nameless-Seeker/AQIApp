package com.example.skeleton.Model.Koin

import com.example.skeleton.Model.KTor.APIs
import com.example.skeleton.Model.KTor.KtorClient
import com.example.skeleton.Model.Repository.DataStoreRepository
import com.example.skeleton.Model.Repository.NetworkRepository
import com.example.skeleton.Model.Repository.RoomRepository
import com.example.skeleton.Model.Repository.TinkCryptoManager
import com.example.skeleton.Model.Retrofit.RetrofitClient
import com.example.skeleton.Model.Room.NoteDatabase
import com.example.skeleton.ViewModel.MyViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Crypto
    single { TinkCryptoManager(get()) }

    // DataStore
    single { DataStoreRepository(get(), get()) }

    // KTor
    single { KtorClient(get()) }
    single { get<KtorClient>().client }
    single { APIs(get()) }

    // Retrofit
    single { RetrofitClient(get()) }

    // Database
    single { NoteDatabase.getDatabase(get()) }
    single { get<NoteDatabase>().dao() }

    // Repositories
    single { RoomRepository(get()) }
    single { NetworkRepository(get()) }

    // ViewModel
    viewModel {
        MyViewModel(
            get(), // roomRepo
            get(), // networkRepo
            get()  // dataStoreRepo
        )
    }
}