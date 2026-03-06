package com.example.easygame.di

import com.example.easygame.domain.usecase.BuyItemUseCase
import com.example.easygame.domain.usecase.ControlGameUseCase
import com.example.easygame.domain.usecase.GetStoreItemListUseCase
import com.example.easygame.domain.usecase.SelectItemUseCase
import com.example.easygame.ui.screen.game_detail.GameDetailViewModel
import com.example.easygame.ui.screen.high_score.HighScoreViewModel
import com.example.easygame.ui.screen.setting.SettingsViewModel
import com.example.easygame.ui.screen.store.StoreViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { BuyItemUseCase(get(), get(), get()) }
    factory { GetStoreItemListUseCase(get(), get()) }
    factory { ControlGameUseCase(get(), get(), get()) }
    factory { SelectItemUseCase(get(), get()) }

    viewModel { GameDetailViewModel(get(), get()) }
    viewModel { StoreViewModel(get(), get(), get()) }
    viewModel { HighScoreViewModel(get()) }
    viewModel { SettingsViewModel() }
}
