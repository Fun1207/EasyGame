package com.example.easygame.domain.model

sealed class GetItemState {
    object Idle : GetItemState()
    data class Success(val remoteGameObjectList: List<RemoteGameObject>) : GetItemState()
    data class Error(val message: String) : GetItemState()
}
