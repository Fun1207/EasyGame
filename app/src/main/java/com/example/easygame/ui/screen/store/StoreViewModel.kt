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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
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
    private val _enableBuyButtonFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val enableBuyButtonFlow: StateFlow<Boolean> = _enableBuyButtonFlow
    private val _purchaseItemFlow: MutableStateFlow<PurchaseState> =
        MutableStateFlow(PurchaseState.Idle)
    val purchaseItemFlow: StateFlow<PurchaseState> = _purchaseItemFlow
    val coinFlow = buyItemUseCase.ownedCoin.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0L
    )

    private val canBuyItem: Boolean
        get() {
            val buyingPrice = _selectedItemFlow.value?.price ?: 0L
            val ownedCoin = coinFlow.value
            return ownedCoin >= buyingPrice
        }

    init {
        fetchRemoteGameObjectList()
    }

    private fun fetchRemoteGameObjectList() = viewModelScope.launch(Dispatchers.IO) {
        when (val getItemState = getItemUseCase.getItemList()) {
            is GetItemState.Idle -> Unit
            is GetItemState.Success -> _itemListStateFlow.emit(getItemState.remoteGameObjectList)
            is GetItemState.Error -> Unit
        }
    }

    fun buyItem() {
        val buyingItem = _selectedItemFlow.value ?: return
        if (buyingItem.isPurchased || !canBuyItem) return
        viewModelScope.launch {
            _purchaseItemFlow.value = PurchaseState.Loading
            _purchaseItemFlow.value = buyItemUseCase.buyItem(buyingItem).also { state ->
                if (state is PurchaseState.Success) fetchRemoteGameObjectList()
            }
        }
    }

    fun setSelectedGameObject(id: String?) {
        val selectedItem = _itemListStateFlow.value.find { it.id == id }
        _selectedItemFlow.value = selectedItem
        _enableBuyButtonFlow.value = canBuyItem && selectedItem?.isPurchased != true
    }
}
