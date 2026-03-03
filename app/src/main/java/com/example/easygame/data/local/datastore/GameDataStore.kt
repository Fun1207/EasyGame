package com.example.easygame.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.easygame.domain.util.DEFAULT_BASKET_ID
import com.example.easygame.domain.util.GAME_DATA_STORE
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = GAME_DATA_STORE)

class GameDataStore(private val context: Context) {

    val coins = context.dataStore.data.runCatching {
        map { it[COINS_KEY] ?: 0L }
    }.getOrDefault(flowOf(0L))

    suspend fun setCoins(coins: Long) = context.dataStore.edit {
        it[COINS_KEY] = coins
    }

    val selectedBasketId = context.dataStore.data.runCatching {
        map { it[SELECTED_BASKET_KEY] ?: DEFAULT_BASKET_ID }
    }.getOrDefault(flowOf(DEFAULT_BASKET_ID))

    suspend fun setSelectedBasket(basketId: String) = context.dataStore.edit {
        it[SELECTED_BASKET_KEY] = basketId
    }

    private companion object {
        val COINS_KEY = longPreferencesKey("user_coins")
        val SELECTED_BASKET_KEY = stringPreferencesKey("selected_basket")
    }
}
