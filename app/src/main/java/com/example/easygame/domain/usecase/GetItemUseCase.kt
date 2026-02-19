package com.example.easygame.domain.usecase

import com.example.easygame.R
import com.example.easygame.data.local.dao.PurchasedObjectDao
import com.example.easygame.data.remote.FirebaseDataSource
import com.example.easygame.domain.model.GameObjectType
import com.example.easygame.domain.model.GetItemState
import com.example.easygame.domain.model.RemoteGameObject
import kotlinx.coroutines.flow.flow

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
        ),
        RemoteGameObject(
            name = "Apple",
            type = GameObjectType.APPLE.type,
            source = R.drawable.icon_apple,
            isPurchased = true
        ),
        RemoteGameObject(
            name = "Bomb",
            type = GameObjectType.BOMB.type,
            source = R.drawable.icon_bomb,
            isPurchased = true
        ),
        RemoteGameObject(
            name = "Coin",
            type = GameObjectType.COIN.type,
            source = R.drawable.icon_coin,
            isPurchased = true
        )
    )

    fun getItemList() = flow {
        runCatching {
            val firebaseItems = firebaseDataSource.getGameObjectList()
            val purchasedItems = purchasedObjectDao.getPurchasedObjects().map { purchasedItem ->
                purchasedItem.toRemoteGameObject() }
            val resultItems = inAppItemList + (purchasedItems + firebaseItems).distinctBy { it.id }.sortedBy { it.id }
            emit(GetItemState.Success(resultItems))
        }.getOrElse {
            emit(GetItemState.Error(it.message.toString()))
        }
    }
}
