package com.example.easygame.ui.screen.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easygame.domain.model.GameError
import com.example.easygame.domain.model.GameObject
import com.example.easygame.domain.model.PurchaseState
import com.example.easygame.domain.usecase.BuyItemUseCase
import com.example.easygame.domain.usecase.GetStoreItemListUseCase
import com.example.easygame.domain.usecase.SelectItemUseCase
import com.example.easygame.domain.util.DEFAULT_BASKET
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoreViewModel(
    private val getStoreItemListUseCase: GetStoreItemListUseCase,
    private val buyItemUseCase: BuyItemUseCase,
    private val selectItemUseCase: SelectItemUseCase
) : ViewModel() {
    private val _itemListStateFlow = MutableStateFlow<List<GameObject>>(emptyList())
    val itemListStateFlow = _itemListStateFlow.asStateFlow()
    private val _buyItemState = MutableStateFlow<PurchaseState>(PurchaseState.Idle)
    val buyItemState = _buyItemState.asStateFlow()
    private val _buyItemError = MutableStateFlow<GameError?>(null)
    val buyItemError = _buyItemError.asStateFlow()
    val selectedBasketFlow = selectItemUseCase.getSelectedItemFlow().stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = DEFAULT_BASKET
    )
    val ownedCoinFlow = buyItemUseCase.ownedCoinFlow.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = 0L
    )

    init {
        viewModelScope.launch {
            fetchItemList()
        }
    }

    private suspend fun fetchItemList() {
        _itemListStateFlow.update { getStoreItemListUseCase.getItemList() }
    }

    fun buyItem(gameObject: GameObject) {
        if (gameObject.isPurchased) return
        viewModelScope.launch {
            _buyItemState.update { PurchaseState.Loading }
            when (val buyItemState = buyItemUseCase.buyItem(gameObject)) {
                is PurchaseState.Error -> {
                    _buyItemError.update { buyItemState.gameError }
                    _buyItemState.update { buyItemState }
                }
                is PurchaseState.Success -> {
                    fetchItemList()
                    _buyItemState.update { buyItemState }
                }
                else -> Unit
            }
        }
    }

    fun selectedItem(selectedItemId: String) = viewModelScope.launch {
        selectItemUseCase.selectedItem(selectedItemId)
    }

    fun dismissError() {
        _buyItemError.update { null }
    }
}
