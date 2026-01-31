package com.example.easygame.ui.screen.game_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easygame.data.repository.GameSensorManager
import kotlinx.coroutines.launch

class GameDetailViewModel(val gameSensorManager: GameSensorManager) : ViewModel() {

    var circleX by mutableFloatStateOf(0.5f)
    var screenWidthPx by mutableFloatStateOf(1f)
    var screenHeightPx by mutableFloatStateOf(1f)

    init {
        gameSensorManager.startListening()

        viewModelScope.launch {
            gameSensorManager.tiltData.collect { coordinate ->
                updatePosition(coordinate)
            }
        }
    }

    private fun updatePosition(x: Float) {
        circleX = (circleX - x * 0.005f).coerceIn(0f, 1f)
    }
}
