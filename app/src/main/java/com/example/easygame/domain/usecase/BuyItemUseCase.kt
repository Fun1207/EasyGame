package com.example.easygame.domain.usecase

import com.example.easygame.data.local.dao.PurchasedObjectDao
import com.example.easygame.data.local.datastore.GameDataStore
import com.example.easygame.data.repository.GameResourceRepository
import com.example.easygame.domain.model.GameError
import com.example.easygame.domain.model.GameObject
import com.example.easygame.domain.model.GameObject.Companion.toPurChasedGameEntity
import com.example.easygame.domain.model.PurchaseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class BuyItemUseCase(
    private val purchasedObjectDao: PurchasedObjectDao,
    private val gameResourceRepository: GameResourceRepository,
    private val gameDataStore: GameDataStore
) {

    val ownedCoinFlow = gameDataStore.coins
    private fun insertPurchasedObject(gameObject: GameObject, path: String?) {
        if (path.isNullOrBlank()) throw Throwable(PATH_EMPTY_ERROR)
        val purchasedEntity = gameObject.toPurChasedGameEntity().copy(localPath = path)
        purchasedObjectDao.insertPurchasedObject(purchasedEntity)
    }

    private suspend fun downloadItem(gameObject: GameObject) {
        val savedPath = gameResourceRepository.saveGameObject(gameObject)
        insertPurchasedObject(gameObject, savedPath)
    }

    suspend fun buyItem(gameObject: GameObject) = withContext(Dispatchers.IO) {
        val ownedCoin = ownedCoinFlow.firstOrNull() ?: 0L
        if (ownedCoin < gameObject.price) {
            val error = GameError(
                message = NOT_ENOUGH_COIN_ERROR,
                title = NOT_ENOUGH_COIN_TITLE,
                code = NOT_ENOUGH_COIN_ERROR_CODE
            )
            return@withContext PurchaseState.Error(error)
        }
        return@withContext runCatching {
            downloadItem(gameObject)
            gameDataStore.setCoins(ownedCoin - gameObject.price)
            return@withContext PurchaseState.Success
        }.getOrElse { throwable ->
            val error = GameError(throwable.message.toString())
            PurchaseState.Error(error)
        }
    }

    companion object {
        const val PATH_EMPTY_ERROR = "Path empty error"
        const val NOT_ENOUGH_COIN_TITLE = "Not Enough Coins!"
        const val NOT_ENOUGH_COIN_ERROR = "Play more or visit the shop to get more."
        const val NOT_ENOUGH_COIN_ERROR_CODE = 1
    }
}
