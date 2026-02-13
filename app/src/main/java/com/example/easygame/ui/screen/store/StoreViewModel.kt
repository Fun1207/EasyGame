package com.example.easygame.ui.screen.store

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easygame.data.remote.FirebaseDataSource
import com.example.easygame.domain.model.PurchaseState
import com.example.easygame.domain.model.RemoteGameObject
import com.example.easygame.domain.usecase.BuyItemUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoreViewModel(
    private val firebaseDataSource: FirebaseDataSource,
    private val buyItemUseCase: BuyItemUseCase
) : ViewModel() {
    var remoteGameObjectList by mutableStateOf<List<RemoteGameObject>>(emptyList())
        private set
    private var _purchasedObjectStateFlow: MutableStateFlow<PurchaseState> =
        MutableStateFlow(PurchaseState.Idle)
    val purchasedObjectStateFlow: StateFlow<PurchaseState> = _purchasedObjectStateFlow


    init {
        fetchRemoteGameObjectList()
    }

    private fun fetchRemoteGameObjectList() = viewModelScope.launch {
        remoteGameObjectList = firebaseDataSource.getGameObjectList()
    }

    fun buyItem(gameObject: RemoteGameObject?) {
        viewModelScope.launch(Dispatchers.IO) {
            buyItemUseCase.buyItem(gameObject ?: return@launch).collect {
                _purchasedObjectStateFlow.value = it
            }
        }
    }
}
