package com.example.easygame.domain.usecase

import com.example.easygame.data.local.dao.PurchasedObjectDao
import com.example.easygame.data.repository.GameResourceRepository
import com.example.easygame.domain.model.PurchaseState
import com.example.easygame.domain.model.RemoteGameObject
import kotlinx.coroutines.flow.flow

class BuyItemUseCase(
    private val purchasedObjectDao: PurchasedObjectDao,
    private val gameResourceRepository: GameResourceRepository
) {

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

    fun buyItem(remoteGameObject: RemoteGameObject) = flow {
        emit(PurchaseState.Loading)
        val remoteVersion = remoteGameObject.getVersion()
        if (remoteGameObject.id !in purchasedObjectDao.getPurchasedObjectIds()) {
            emit(downloadItem(remoteGameObject))
            return@flow
        }
        val localVersion = purchasedObjectDao.getPurchasedObjectVersion(remoteGameObject.id)
        if (remoteVersion == localVersion) {
            emit(PurchaseState.Error(Throwable(ITEM_ALREADY_OWNED_ERROR)))
            return@flow
        }
        emit(downloadItem(remoteGameObject))
    }

    private companion object {
        const val ITEM_ALREADY_OWNED_ERROR = "Item already owned error"
        const val PATH_EMPTY_ERROR = "Path empty error"
        const val INSERTS_ERROR = "Inserts error"
    }
}
