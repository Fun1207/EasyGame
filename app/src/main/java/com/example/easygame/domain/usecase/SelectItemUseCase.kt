package com.example.easygame.domain.usecase

import com.example.easygame.data.local.dao.PurchasedObjectDao
import com.example.easygame.data.local.datastore.GameDataStore
import com.example.easygame.data.local.entities.PurchasedObjectEntity.Companion.toGameObject
import com.example.easygame.domain.model.GameObject
import com.example.easygame.domain.util.DEFAULT_BASKET
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SelectItemUseCase(
    private val gameDataStore: GameDataStore,
    private val purchasedObjectDao: PurchasedObjectDao
) {

    suspend fun getSelectedItem(): GameObject {
        val selectedItemId = gameDataStore.selectedBasketId.firstOrNull() ?: return DEFAULT_BASKET
        val selectedItem =
            purchasedObjectDao.getPurchasedObjectById(selectedItemId) ?: return DEFAULT_BASKET
        return selectedItem.toGameObject()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getSelectedItemFlow() = gameDataStore.selectedBasketId
        .flatMapLatest { selectedId ->
            purchasedObjectDao.getPurchasedObjectByIdFlow(selectedId)
        }.map { purchasedObjectEntity ->
            purchasedObjectEntity?.toGameObject() ?: DEFAULT_BASKET
        }

    suspend fun selectedItem(selectedItemId: String) = withContext(Dispatchers.IO) {
        gameDataStore.setSelectedBasket(selectedItemId)
    }
}
