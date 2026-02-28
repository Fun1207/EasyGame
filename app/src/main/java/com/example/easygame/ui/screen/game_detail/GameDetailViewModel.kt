package com.example.easygame.ui.screen.game_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easygame.R
import com.example.easygame.data.local.dao.SelectedItemDao
import com.example.easygame.domain.model.GameObjectType
import com.example.easygame.domain.usecase.ControlGameUseCase
import com.example.easygame.domain.util.toStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameDetailViewModel(
    private val controlGameUseCase: ControlGameUseCase,
    private val selectedItemDao: SelectedItemDao
) : ViewModel() {

    var basketResource by mutableStateOf<Any>(R.drawable.icon_basket)
        private set
    val basketX = controlGameUseCase.basketX.toStateFlow(viewModelScope)
    val gameObjectList = controlGameUseCase.gameObjectList.toStateFlow(viewModelScope)
    val score = controlGameUseCase.score.toStateFlow(viewModelScope)
    val coin = controlGameUseCase.coin.toStateFlow(viewModelScope)
    val heart = controlGameUseCase.healPoint.toStateFlow(viewModelScope)
    val isGamePaused = controlGameUseCase.isGamePause.toStateFlow(viewModelScope)
    val isGameOver = controlGameUseCase.isGameOver.toStateFlow(viewModelScope)


    init {
        getSelectedItem()
        viewModelScope.launch {
            controlGameUseCase.startGame()
        }
    }

    private fun getSelectedItem() {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedBasket =
                selectedItemDao.getSelectedItemsByType(GameObjectType.BASKET) ?: return@launch
            basketResource = selectedBasket.source ?: return@launch
        }
    }

    fun togglePauseGame(isPause: Boolean) = controlGameUseCase.togglePause(isPause)

    fun quitGame() = viewModelScope.launch {
        controlGameUseCase.quitGame()
    }
}
