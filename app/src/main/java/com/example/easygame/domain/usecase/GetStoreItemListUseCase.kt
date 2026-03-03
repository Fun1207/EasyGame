package com.example.easygame.domain.usecase

import com.example.easygame.data.local.dao.PurchasedObjectDao
import com.example.easygame.data.local.entities.PurchasedObjectEntity.Companion.toGameObject
import com.example.easygame.data.remote.FirebaseDataSource
import com.example.easygame.domain.util.DEFAULT_BASKET
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetStoreItemListUseCase(
    private val firebaseDataSource: FirebaseDataSource,
    private val purchasedObjectDao: PurchasedObjectDao
) {

    suspend fun getItemList() = withContext(Dispatchers.IO) {
        runCatching {
            val firebaseItems = firebaseDataSource.getGameObjectList().map { it.toGameObject() }
            val purchasedItems = purchasedObjectDao.getPurchasedObjects().map { purchasedItem ->
                purchasedItem.toGameObject()
            }
            listOf(DEFAULT_BASKET) + (purchasedItems + firebaseItems)
                .distinctBy { it.id }
                .sortedBy { it.id }
        }.getOrElse {
            listOf(DEFAULT_BASKET)
        }
    }
}
