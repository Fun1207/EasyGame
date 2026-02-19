package com.example.easygame.ui.screen.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easygame.domain.model.GetItemState
import com.example.easygame.domain.model.PurchaseState
import com.example.easygame.domain.model.RemoteGameObject
import com.example.easygame.domain.usecase.BuyItemUseCase
import com.example.easygame.domain.usecase.GetItemUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoreViewModel(
    private val getItemUseCase: GetItemUseCase,
    private val buyItemUseCase: BuyItemUseCase
) : ViewModel() {
    private val _itemListStateFlow: MutableStateFlow<List<RemoteGameObject>> =
        MutableStateFlow(emptyList())
    val itemListStateFlow: StateFlow<List<RemoteGameObject>> = _itemListStateFlow
    private val _selectedItemFlow: MutableStateFlow<RemoteGameObject?> = MutableStateFlow(null)
    val selectedItemFlow: StateFlow<RemoteGameObject?> = _selectedItemFlow
    private val _buyItemFlow: MutableStateFlow<PurchaseState> =
        MutableStateFlow(PurchaseState.Idle)
    val buyItemFlow: StateFlow<PurchaseState> = _buyItemFlow

    init {
        fetchRemoteGameObjectList()
    }

    private fun fetchRemoteGameObjectList() = viewModelScope.launch(Dispatchers.IO) {
        getItemUseCase.getItemList().collect { state ->
            if (state is GetItemState.Success) _itemListStateFlow.emit(state.remoteGameObjectList)
        }
    }

    fun buyItem() {
        viewModelScope.launch(Dispatchers.IO) {
            buyItemUseCase.buyItem(_selectedItemFlow.value ?: return@launch).collect {
                _buyItemFlow.value = it
            }
            fetchRemoteGameObjectList()
        }
    }

    fun setSelectedGameObject(id: String?) {
        _selectedItemFlow.value = _itemListStateFlow.value.find { it.id == id }
    }
}
