package com.example.easygame.ui.screen.game_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easygame.data.model.Apple
import com.example.easygame.data.repository.GameSensorManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameDetailViewModel(val gameSensorManager: GameSensorManager) : ViewModel() {

    var arrowX by mutableFloatStateOf(0.5f)
        private set
    var appleList by mutableStateOf(listOf<Apple>())
        private set
    var score by mutableIntStateOf(0)
        private set
    var isGamePaused by mutableStateOf(false)
        private set
    var isGameOver: Boolean = false
        private set
    var hitBoxSize by mutableFloatStateOf(0f)

    init {
        gameSensorManager.startListening()
        viewModelScope.launch {
            gameSensorManager.tiltData.collect { coordinate ->
                arrowX = (arrowX - coordinate * 0.005f).coerceIn(0f, 1f)
            }
        }
        viewModelScope.launch {
            while (!isGameOver) {
                if (!isGamePaused) updateGameLogic()
                delay(16)
            }
        }
    }

    override fun onCleared() {
        gameSensorManager.stopListening()
        isGameOver = true
        super.onCleared()
    }

    private fun updateGameLogic() {
        val currentApples = appleList.map { it.copy(y = it.y + 0.008f) }
        appleList = currentApples.filter { apple ->
            if (apple.y < 1f - hitBoxSize * 0.9f) {
                return@filter true
            }
            if (apple.x >= arrowX - hitBoxSize && apple.x <= arrowX + hitBoxSize) {
                score += 1
                return@filter false
            }
            apple.y < 1.1f
        }
        if (appleList.size < 5 && Random.nextInt(100) < 4) {
            val newApple = Apple(x = Random.nextFloat(), y = -0.1f)
            appleList = appleList + newApple
        }
    }

    fun togglePauseGame(isPause: Boolean) {
        isGamePaused = isPause
        if (isPause) gameSensorManager.stopListening()
        else gameSensorManager.startListening()
    }
}
