package com.example.easygame.domain.model

sealed class PurchaseState {
    object Idle : PurchaseState()
    object Loading : PurchaseState()
    data class Error(val gameError: GameError) : PurchaseState()
    object Success : PurchaseState()
}
