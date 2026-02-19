package com.example.easygame.domain.usecase

import com.example.easygame.R
import com.example.easygame.data.local.dao.PurchasedObjectDao
import com.example.easygame.data.remote.FirebaseDataSource
import com.example.easygame.domain.model.GameObjectType
import com.example.easygame.domain.model.GetItemState
import com.example.easygame.domain.model.RemoteGameObject

class GetItemUseCase(
    private val firebaseDataSource: FirebaseDataSource,
    private val purchasedObjectDao: PurchasedObjectDao
) {

    private val inAppItemList = listOf(
        RemoteGameObject(
            name = "Basket",
            type = GameObjectType.BASKET.type,
            source = R.drawable.icon_basket,
            isPurchased = true
        )
    )

    suspend fun getItemList() = runCatching {
        val firebaseItems = firebaseDataSource.getGameObjectList()
        val purchasedItems = purchasedObjectDao.getPurchasedObjects().map { purchasedItem ->
            purchasedItem.toRemoteGameObject()
        }
        val resultItems =
            inAppItemList + (purchasedItems + firebaseItems).distinctBy { it.id }.sortedBy { it.id }
        GetItemState.Success(resultItems)
    }.getOrElse {
        GetItemState.Error(it.message.toString())
    }
}
