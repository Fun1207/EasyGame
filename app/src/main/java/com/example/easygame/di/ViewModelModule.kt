package com.example.easygame.di

import com.example.easygame.ui.screen.game_detail.GameDetailViewModel
import com.example.easygame.ui.screen.store.StoreViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { GameDetailViewModel(get(), get(), get()) }
    viewModel { StoreViewModel(get(), get(), get()) }
}
