package com.example.easygame.domain.usecase

import com.example.easygame.data.local.dao.PurchasedObjectDao
import com.example.easygame.data.local.datastore.CoinDataStore
import com.example.easygame.data.repository.GameResourceRepository
import com.example.easygame.domain.model.PurchaseState
import com.example.easygame.domain.model.RemoteGameObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class BuyItemUseCase(
    private val purchasedObjectDao: PurchasedObjectDao,
    private val gameResourceRepository: GameResourceRepository,
    private val coinDataStore: CoinDataStore
) {

    val ownedCoin = coinDataStore.coins
    private fun insertPurchasedObject(remoteGameObject: RemoteGameObject, path: String?) {
        if (path.isNullOrBlank()) throw Throwable(PATH_EMPTY_ERROR)
        val purchasedEntity =
            remoteGameObject.toPurchasedEntityOrNull(path) ?: throw Throwable(INSERTS_ERROR)
        purchasedObjectDao.insertPurchasedObject(purchasedEntity)
    }

    private suspend fun downloadItem(remoteGameObject: RemoteGameObject): PurchaseState {
        val result = gameResourceRepository.saveGameObject(remoteGameObject)
        val savedPath = result.getOrElse { throwable ->
            return PurchaseState.Error(throwable)
        }
        return runCatching {
            insertPurchasedObject(remoteGameObject, savedPath)
            PurchaseState.Success(remoteGameObject)
        }.getOrElse { throwable ->
            PurchaseState.Error(throwable)
        }
    }

    suspend fun buyItem(remoteGameObject: RemoteGameObject) = withContext(Dispatchers.IO) {
        val ownedCoin = ownedCoin.firstOrNull() ?: 0L
        val buyingPrice = remoteGameObject.price ?: 0L
        if (ownedCoin < buyingPrice) return@withContext PurchaseState.Error(
            Throwable(NOT_ENOUGH_COIN_ERROR)
        )
        val result = downloadItem(remoteGameObject)
        if (result is PurchaseState.Success) runCatching {
            coinDataStore.setCoins(ownedCoin - buyingPrice)
        }.getOrElse { throwable ->
            return@withContext PurchaseState.Error(throwable)
        }
        return@withContext result
    }

    private companion object {
        const val PATH_EMPTY_ERROR = "Path empty error"
        const val INSERTS_ERROR = "Inserts error"
        const val NOT_ENOUGH_COIN_ERROR = "No coin error"
    }
}
