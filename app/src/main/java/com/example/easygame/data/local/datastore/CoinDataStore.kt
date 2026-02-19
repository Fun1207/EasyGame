package com.example.easygame.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class CoinDataStore(private val context: Context) {

    val coins = context.dataStore.data.runCatching {
        map { it[COINS_KEY] ?: 0L }
    }.getOrDefault(flowOf(0L))

    suspend fun setCoins(coins: Long) = context.dataStore.edit {
        it[COINS_KEY] = coins
    }

    private companion object {
        val COINS_KEY = longPreferencesKey("user_coins")
    }
}
