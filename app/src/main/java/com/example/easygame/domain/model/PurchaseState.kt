package com.example.easygame.domain.model

sealed class PurchaseState {
    object Idle : PurchaseState()
    object Loading : PurchaseState()
    data class Error(val throwable: Throwable?) : PurchaseState()
    data class Success(val remoteGameObject: RemoteGameObject) : PurchaseState()
}
